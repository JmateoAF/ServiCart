# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

ServiCart ("Sistema de Pago de Servicios Básicos con Carrito de Compras") is a JavaFX desktop
app built for a university OOP course (Programación III, Universidad de Cuenca). It simulates a
platform where clients accumulate utility bills (electricity, water, internet, garbage) into a
shopping cart and pay them together.

## Build, run, and environment

- Build tool: Maven (`pom.xml`), no wrapper included. Java source/target level 25.
- Requires **JDK 26** and **JavaFX 26** to run identically on Linux/Windows — set this JDK in
  IntelliJ.
- Entry point is `src/main/java/servicart/Main.java`. Note it has **no `package` declaration**
  and uses the JDK 25+ implicit/unnamed-class `void main()` form (no `public static void main`,
  no enclosing class) — this is intentional, not an error. The IntelliJ run configuration
  (`.idea/runConfigurations/Main.xml`) launches it as class `Main` with VM options
  `--enable-native-access=ALL-UNNAMED --sun-misc-unsafe-memory-access=allow`.
- There is no `javafx-maven-plugin` or exec plugin configured in `pom.xml` — the project is run
  from the IDE (IntelliJ run configuration), not via a Maven command. When adding build/run
  automation, check with the user before introducing a new plugin.
- No test framework/dependency is configured and there are no test sources — there is currently
  no `mvn test` target to run.
- No linter is configured.

## Data layer: dual persistence, switched at runtime

The app supports two interchangeable persistence backends, chosen by the user at login
(`cmbBaseDatos` in `loginCliente.fxml`, defaulting to `"SQLite"`):

- **`"SQLite"`** — via `sqlite-jdbc`, DB file at `sql/data.db`. Schema in `sql/dbsetup.sql`,
  seed data in `sql/datosPrueba.sql`. Both scripts are executed by
  `servicart.data.sqlite.ConexionSQLite.inicializarBaseDeDatos()` the first time SQLite is
  selected in a session.
- **`"Binario"`** — Java object serialization to `.bin` files under `bin/` (one file per entity,
  e.g. `bin/clientes.bin`). Managed through `servicart.data.binary.ConexionBinario` (atomic
  writes via temp-file + `ATOMIC_MOVE`) and `servicart.data.binary.GenericBinarioDAO<T>`, which
  every binary DAO extends. Seeded by `servicart.data.DatosSeeder`.

`servicart.data.FactoryDAO` is a Simple Factory that is the single place deciding which
implementation to hand out: `FactoryDAO.configurar(nombreBd)` sets the active backend (called from
`BdService.configurarBaseDatos`, invoked by controllers after login), and
`FactoryDAO.getDAO(EntityClass.class)` returns the right `CrudDAO<T>` (SQLite or binary) for that
entity. **Every entity DAO has two parallel implementations** — one in `data.sqlite`, one in
`data.binary` — both implementing `servicart.data.interfaces.CrudDAO<T>`
(`save`/`findId`/`findAll`/`update`/`delete`). When adding a new entity or DAO method, both
implementations must be added/updated together, and `FactoryDAO` must be extended to route to them.

Because the backend is a runtime choice, domain services never hardcode a DAO implementation —
they always fetch DAOs via `FactoryDAO.getDAO(...)`.

## Layered architecture

The codebase is a strict multi-tier design (Presentación / Lógica / Datos), and each tier has its
own DTO/mapper pair — data is deliberately re-mapped at each boundary rather than passing entities
or view state across layers directly:

```
ui.controllers (JavaFX/FXML)
   ↕ ui.mappers  ↔ ui.viewmodels           (view-model ↔ domain DTOs)
domain.services.{cliente,empresa,admin}     (business logic, implements domain.interfaces)
   ↕ domain.mappers ↔ domain.dtos.{entradas,retornos}   (entity ↔ domain DTOs)
data.{sqlite,binary} DAOs (via FactoryDAO)  ↔ entities
```

- **`entities`** — plain domain model classes (`Cliente`, `Contrato`, `Factura`, `Abono`,
  `ServicioCatalogo`, `Empresa`, `CorteServicio`, `InteresMora`, `Carrito`, `Admin`...),
  `Serializable` (required for the binary backend). Enums live in `entities.enums`
  (`EstadoFactura`, `EstadoCorte`, `ModalidadPago`, `TipoServicio`, `TipoValorFactura`,
  `CausaTerminacion`).
- **`domain.interfaces`** — service contracts per use case (`LoginCliente`, `PanelCliente`,
  `CarritoCliente`, `Checkout`, `ContratoCliente`, `PerfilCliente`, `LoginAdmin`,
  `CalculoStrategy`, `Observador`, `Identificable`).
- **`domain.services.{cliente,empresa,admin}`** — one `*Imp` class per interface, implementing
  the use case; plumbing services like `ContratoService`, `FacturacionService`, `CorteService`,
  `EmpresaService`, `MoraService` hold business logic shared across use cases and jobs.
- **`domain.dtos.entradas` / `domain.dtos.retornos`** — Java `record`s used as input/output of
  domain services; converted to/from entities by `domain.mappers`.
- **`ui.viewmodels.{admin,cliente}`** and **`ui.mappers`** — convert between JavaFX-facing view
  models and domain DTOs; controllers never touch entities or DAOs directly.
- **`ui.controllers`** — FXML controllers, constructed exclusively through
  `servicart.ui.controllers.Navegador.crearControlador`, which is set as the FXML controller
  factory. Controllers requiring constructor-injected services (login, carrito, checkout, etc.)
  must be registered there by `Class` in `Navegador.crearControlador`; anything else falls back to
  a no-arg constructor. Screen navigation goes through `Navegador.irA("views/.../file.fxml")`,
  which swaps the root of the single `Stage` set up in `Main`.
- `servicart.ui.SesionCliente` is a static holder for the currently logged-in client's `cédula`
  (there's no per-window session object).

## Other design patterns to be aware of

- **Strategy** — `CalculoStrategy` (`CalculoFijo`, `CalculoVariable`) computes a `ServicioCatalogo`
  bill amount from consumption; selected per service.
- **Observer** — `entities.SujetoNotificable` (subject) / `domain.interfaces.Observador`
  (observer, implemented by `NotificadorService`) notifies on invoice emission
  (`obs.actualizar(factura)`).
- **Background job** — `domain.services.empresa.GestionAutomaticaEmpresaJob` runs a daily cycle
  (invoice emission on the 20th of each month, late-fee/mora application, service cutoffs for
  overdue accounts, contract termination after 30 days cut off) on a daemon
  `ScheduledExecutorService`. It's started once via
  `GestorProcesosEmpresa.iniciarSiEsNecesario()` (called after a successful login) and stopped via
  `GestorProcesosEmpresa.detener()` (wired to the primary `Stage`'s close request in `Main`).
  All four sub-tasks run inside a single synchronized block per cycle (`CANDADO_BD`) and each is
  wrapped so one failing task doesn't block the others.

## Conventions

- Domain/business code, comments, and identifiers are in **Spanish** (matching the university
  assignment); keep new code consistent with this.
- Entity IDs: numeric-ID entities implement `domain.interfaces.Identificable`; the binary DAO
  layer auto-increments IDs for those, mirroring `AUTOINCREMENT` in the SQLite schema.
- FXML views live under `src/main/resources/views/{admin,cliente}`; static assets (icons, images,
  `styles.css`) live under `src/main/resources/assets`.