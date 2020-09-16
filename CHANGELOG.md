# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security

## [1.2.0]
### Added
 - [#7] Output syntax highlighting

### Fixed
 - [#11] Plugin won't create a null file anymore on windows. 
 - [#16] Only 1 tinker console will be open at all times.
 - [#18] Added the LARAVEL_START constant to the tinker run script.
 - [#19] Colors in output window now reset to IDE colors when running.
 - [#12] [#20] Fixes `Class 'Laravel\Tinker\ClassAliasAutoloader' not found`
   - By just checking if it exists before loading it
   - Should fix the plugin for laravel/tinker versions `<= 1.0.1`
   
## [1.1.1]
### Added
 - Your last command will now be saved (on per-project level)
 - Non-obtrusive reminder you can support this package.
 
### Changed
 - Output window now (hopefully) always inherits the font and color of the IDE

### Fixed
 - The link to PHP settings in error dialogs now *acually* links to the correct settings page.
 - Output showing slow on larger outputs

## [1.1.0]

### Added
 - Run button at the opening tag of the tinker console

### Changed
 - Cleaned output of tinker output window
 - PHP Runner script is now a php file, makes it easier to work with in the IDE.
 
### Fixed
 - Default keybinding in Mac OS
 - Toolwindow not opening automatically if not opened before
 - Leaving zombie processes (something pcntl did)
 - Silent crash when docker was not set
 - Tinker process not shutting down because of waiting for non-existent input

## [1.0.0]
### Added
 - A correct way to interact with remote interpreters

### Changed
 - Made errors more to the point
 - Better code splitting in message balloons
 - More text in the readme
 - Better formatting in tinker output
 
### Fixed
 - Laravel now actually gets bootstrapped
 - Removed needless output from interpreter, like docker startup messages
 - Made output toolwindow font show in readable colors
 - Campatibility issues that arose in 0.0.1-ALPHA
 
 
## [0.0.1-ALPHA] - 2020-08-27
### Added
 - Everything