# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.3.4]
### Fixed
- Docker workaround, see [github issue](https://github.com/Roboroads/laravel-tinker/issues/46)

## [2.3.3]
### Removed

## [2.3.2]
### Changed
- Updated dependencies.

### Fixed
- Qodana version which stopped the build from completing

## [2.3.1]

## [2.3.0]
### Added
- [[#112]](https://github.com/Roboroads/laravel-tinker/issues/112) Adds <kbd>Open new console</kbd> and <kbd>Reopen last console</kbd> actions to the tinker output toolwindow.

### Changed
- Cleaned up the output toolwindow a little bit.
- Added dependabot updates [#113](https://github.com/Roboroads/laravel-tinker/pull/113) & [#114](https://github.com/Roboroads/laravel-tinker/pull/114).

### Fixed
- [[#110]](https://github.com/Roboroads/laravel-tinker/issues/110) Fixes typo in caster for the Model class.

## [2.2.2]
### Added
- Way of migrating to newer versions of this plugin.
- [[#105]](https://github.com/Roboroads/laravel-tinker/pull/105) Adds the stringable caster added in [laravel/tinker v2.6.1](https://github.com/laravel/tinker/pull/121).
  - Thanks [@emargareten](https://github.com/emargareten).

### Changed
- Restyling of Tinker console tabs.

### Fixed
- [[#61]](https://github.com/Roboroads/laravel-tinker/issues/61) Fixes re-indexing issue causing infinite errors and 100% CPU.
  - Big thanks to [@fawzanm](https://github.com/fawzanm) who helped me figure out a reproduction method and testing my fix.

## [2.2.1]
### Added
- [[#96]](https://github.com/Roboroads/laravel-tinker/issues/96) Added compatibility with newer API versions.

## [2.2.0]
### Added
- [[#34]](https://github.com/Roboroads/laravel-tinker/issues/34) Setting to change tinker execution root.

### Fixed
- [[#75]](https://github.com/Roboroads/laravel-tinker/issues/75) Empty directory list of tinker consoles resulting in fatal error.

## [2.1.0]
### Added
- Dependabot.
- [[#35]](https://github.com/Roboroads/laravel-tinker/issues/35) Setting to enable/disable line wrapping in output window.
- [[#33]](https://github.com/Roboroads/laravel-tinker/issues/33) Adds way for Patreon supporters to disable the support plug.

### Changed
- [[#53]](https://github.com/Roboroads/laravel-tinker/issues/53) Updates compatibility with 2021.1.
- Updated all dependencies.

### Removed
- Tinker Consoles having an IS_TINKER_CONSOLE userdata key.

### Fixed
- [[#47]](https://github.com/Roboroads/laravel-tinker/issues/47) [[#40]](https://github.com/Roboroads/laravel-tinker/issues/40) `<` and `>` are now replaced by `&gt;` and `&lt;` to make them not parse as HTML in tinker output.
- [[#40]](https://github.com/Roboroads/laravel-tinker/issues/40) Fixes always showing output on the last opened project when having multiple projects open.
- [[#41]](https://github.com/Roboroads/laravel-tinker/issues/41) When pressing the x icon on the progress bar to stop the PHP execution, the process now gets destroyed within 250ms.

## [2.0.1]
### Changed
- New branding by [Joey "Veritas" de Vries](https://joeyveritas.nl).

### Fixed
- [[#50]](https://github.com/Roboroads/laravel-tinker/issues/50) Tinker Create/Smart action overwriting its own files from a previous session.
- Tinker files not being PHP when project reloads.

## [2.0.0]
### Added
- BREAKING: [[#39]](https://github.com/Roboroads/laravel-tinker/issues/39) Tinker consoles are now available under "Scratches and Consoles".
- [[#37]](https://github.com/Roboroads/laravel-tinker/issues/37) [[#38]](https://github.com/Roboroads/laravel-tinker/issues/38) When selecting text and running the `Smart Action` a new console opens with your selected text.
  - This can also be achieved by selecting text, right-clicking and selecting `Open Tinker Console With Selection`.

### Changed
- BREAKING: Every action is now independently listed.
  - The default action is now "Smart Action" which will have kind-of the same functionality as you have been using it up till now.
- Every action this plugin can do is now individually bindable in your keymap.

### Removed
- BREAKING: Per-project cache (which stored your last executed tinker code).
  - You can probably find your last snippet still in `%Project%/.idea/laravel-tinker.xml` if you really need it!
  - `%Project%/.idea/laravel-tinker.xml` can be deleted otherwise.

### Other
- Reorganization of a lot of classes.
- Dependencies have been updated.

## [1.3.1]
### Changed
- Updated dependencies.

### Fixed
- Compatibility with 2020.3.

## [1.3.0]
### Added
- Laravel Tinker settings page (under Tools).
- [[#30]](https://github.com/Roboroads/laravel-tinker/issues/30) Ability to disable start and finish messages in output.
- [[#21]](https://github.com/Roboroads/laravel-tinker/issues/21) Ability to add a shortcut to close all involving Tinker.

### Changed
- Dependencies updated to the latest versions.

### Fixed
- The title of the tinker screen was editable - now it isn't.
- [[#28]](https://github.com/Roboroads/laravel-tinker/issues/28) Ending your tinker console with a comment now works correctly again.
  - Credits to [@emargareten](https://github.com/emargareten).

## [1.2.1]
### Added
- [[#25]](https://github.com/Roboroads/laravel-tinker/issues/25) When running tinker it will now save all documents before executing.

### Fixed
- [[#23]](https://github.com/Roboroads/laravel-tinker/issues/23) Fixed a bug where output would display twice when the output highlighter didn't find output to highlight.

## [1.2.0]
### Added
- [[#7]](https://github.com/Roboroads/laravel-tinker/issues/7) Output syntax highlighting.

### Fixed
- [[#11]](https://github.com/Roboroads/laravel-tinker/issues/11) Plugin won't create a null file anymore on windows. 
- [[#16]](https://github.com/Roboroads/laravel-tinker/issues/16) Only 1 tinker console will be open at all times.
- [[#18]](https://github.com/Roboroads/laravel-tinker/issues/18) Added the LARAVEL_START constant to the tinker run script.
- [[#19]](https://github.com/Roboroads/laravel-tinker/issues/19) Colors in output window now reset to IDE colors when running.
- [[#12]](https://github.com/Roboroads/laravel-tinker/issues/12) [[#20]](https://github.com/Roboroads/laravel-tinker/issues/20) Fixes `Class 'Laravel\Tinker\ClassAliasAutoloader' not found`
  - By just checking if it exists before loading it.
  - Should fix the plugin for laravel/tinker versions `<= 1.0.1`.

## [1.1.1]
### Added
- [[#10]](https://github.com/Roboroads/laravel-tinker/issues/10) Your last command will now be saved (on per-project level).
- Non-obtrusive reminder you can support this package.
 

### Changed
- [[#6]](https://github.com/Roboroads/laravel-tinker/issues/6) Output window now (hopefully) always inherits the font and color of the IDE

### Fixed
- The link to PHP settings in error dialogs now *acually* links to the correct settings page.
- [[#9]](https://github.com/Roboroads/laravel-tinker/issues/9) Output showing slow on larger outputs.

## [1.1.0]
### Added
- Run button at the opening tag of the tinker console.

### Changed
- Cleaned output of tinker output window
- PHP Runner script is now a php file, makes it easier to work with in the IDE.
 

### Fixed
- Default keybinding in Mac OS.
- Toolwindow not opening automatically if not opened before.
- Leaving zombie processes (something pcntl did).
- Silent crash when docker was not set.
- Tinker process not shutting down because of waiting for non-existent input.

## [1.0.0]
### Added
- [[#1]](https://github.com/Roboroads/laravel-tinker/issues/1) A correct way to interact with remote interpreters.

### Changed
- Made errors more to the point.
- Better code splitting in message balloons.
- More text in the readme.
- Better formatting in tinker output.
 

### Fixed
- Laravel now actually gets bootstrapped.
- Removed needless output from interpreter, like docker startup messages.
- Made output toolwindow font show in readable colors.
- Campatibility issues that arose in 0.0.1-ALPHA.

## [0.0.1-ALPHA]
### Added
- Everything.
