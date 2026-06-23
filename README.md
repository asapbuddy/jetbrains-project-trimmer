# Rider Solution Prefix Trimmer

Tiny Rider/IntelliJ Platform plugin that hides configured project-name prefixes in the Project/Solution tree.

Example:

```text
Order.Kuper.Adapter.Api            -> Api
Order.Kuper.Adapter.Application    -> Application
Order.Kuper.Adapter.Infrastructure -> Infrastructure
Order.Kuper.Adapter.Worker         -> Worker
```

The plugin only changes how tree nodes are displayed. It does not rename projects, edit `.sln` files, edit `.csproj` files, or affect builds/references.

## Build

This project is a standard Gradle IntelliJ plugin project:

```bash
gradle buildPlugin
```

The installable ZIP will be created under:

```text
build/distributions/
```

If you prefer a wrapper, run this once in the project directory:

```bash
gradle wrapper
./gradlew buildPlugin
```

## Install In Rider

1. Build the plugin ZIP.
2. Open Rider.
3. Go to `Settings | Plugins`.
4. Open the gear menu and choose `Install Plugin from Disk...`.
5. Select the ZIP from `build/distributions`.

## Configure

Use either:

- `Tools | Set Solution Prefix to Hide...`
- `Settings | Tools | Solution Prefix Trimmer`

Enter one prefix per line, for example:

```text
Order.Kuper.Adapter
```

The plugin treats `Order.Kuper.Adapter` and `Order.Kuper.Adapter.` the same way.
