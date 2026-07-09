# Rider Solution Prefix Trimmer

Плагин для JetBrains Rider, который скрывает повторяющиеся префиксы в дереве Solution/Project.

Он полезен для решений, где проекты названы полными namespace-подобными именами, а в Rider хочется видеть короткие и читаемые названия:

```text
Company.Project.Type.Api            -> Api
Company.Project.Type.Application    -> Application
Company.Project.Type.Infrastructure -> Infrastructure
Company.Project.Type.Worker         -> Worker
```

Плагин меняет только отображение узлов в дереве проекта. Он не переименовывает проекты, не редактирует `.sln` или `.csproj`, не меняет references и не влияет на сборку.

## Установка

1. Скачайте zip-архив плагина из GitHub Releases:
   <https://github.com/asapbuddy/jetbrains-project-trimmer/releases>
2. Откройте Rider.
3. Перейдите в `Settings | Plugins`.
4. Нажмите на шестеренку и выберите `Install Plugin from Disk...`.
5. Выберите скачанный zip-архив.
6. Перезапустите Rider, если IDE попросит это сделать.

## Настройка префиксов

Префиксы можно указать двумя способами:

- через меню `Tools | Set Solution Prefix to Hide...`
- через настройки `Settings | Tools | Solution Prefix Trimmer`

Указывайте один префикс на строку:

```text
Company.Project.Type
Another.Long.Prefix
```

Плагин одинаково обрабатывает префиксы с точкой на конце и без нее. Например, `Company.Project.Type` и `Company.Project.Type.` дадут один и тот же результат.

Если имя проекта не начинается ни с одного из указанных префиксов, оно останется без изменений.

## Глобальные и проектные префиксы

Префиксы живут на двух уровнях:

- `Settings | Tools | Solution Prefix Trimmer` — глобальные префиксы, общие для всей IDE. Задаются один раз и действуют во всех проектах, включая каждый новый git worktree.
- `Settings | Tools | Solution Prefix Trimmer | Project Overrides` — настройки текущего проекта. Чекбокс `Use global prefixes` включен по умолчанию; снимите его, чтобы задать проекту собственный список.

Пункт меню `Tools | Set Solution Prefix to Hide...` пишет в тот уровень, который сейчас действует для проекта: в глобальный, если проект следует глобальным префиксам, и в проектный, если он их переопределяет.

Проекты, настроенные в версиях плагина без глобального уровня, продолжают использовать свои префиксы: у них `Use global prefixes` выключен. Чтобы перевести такой проект на общий список, включите чекбокс вручную.

## Пример

Настройка:

```text
Company.Project.Type
```

До:

```text
Company.Project.Type.Api
Company.Project.Type.Application
Company.Project.Type.Infrastructure
Company.Project.Type.Worker
```

После:

```text
Api
Application
Infrastructure
Worker
```

## Сборка из исходников

Для сборки нужен JDK 21.

```bash
./gradlew buildPlugin
```

Готовый zip-архив появится в:

```text
build/distributions/
```

Для полной проверки проекта:

```bash
./gradlew build
```
