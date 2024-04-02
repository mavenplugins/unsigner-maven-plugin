# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

<!-- Format restrictions - see https://common-changelog.org and https://keepachangelog.com/ for details -->
<!-- Each Release must start with a line for the release version of exactly this format: ## [version] -->
<!-- The subsequent comment lines start with a space - not to irritate the release scripts parser!
 ## [major.minor.micro]
 <empty line> - optional sub sections may follow like:
 ### Added:
 - This feature was added
 <empty line>
 ### Changed:
 - This feature was changed
 <empty line>
 ### Removed:
 - This feature was removed
 <empty line>
 ### Fixed:
 - This issue was fixed
 <empty line>
 <empty line> - next line is the starting of the previous release
 ## [major.minor.micro]
 <empty line>
 <...>
 !!! In addition the compare URL links are to be maintained at the end of this CHANGELOG.md as follows.
     These links provide direct access to the GitHub compare vs. the previous release.
     The particular link of a released version will be copied to the release notes of a release accordingly.
     At the end of this file appropriate compare links have to be maintained for each release version in format:
 
  +-current release version
  |
  |                   +-URL to this repo               previous release version tag-+       +-current release version tag
  |                   |                                                             |       |
 [major.minor.micro]: https://github.com/mavenplugins/unsigner-maven-plugin/compare/vM.N.u..vM.N.u
-->
<!--
## [Unreleased]

### Additions
- TBD

### Changes
- TBD

### Deprecated
- TBD

### Removals
- TBD

### Fixes
- TBD

### Security
- TBD
-->

## [Unreleased]

### Changes
- TBD


## [1.0]
<!-- !!! Align version in badge URLs as well !!! -->
[![1.0 Badge](https://img.shields.io/nexus/r/io.github.mavenplugins/unsigner-maven-plugin?server=https://s01.oss.sonatype.org&label=Maven%20Central&queryOpt=:v=1.0)](https://central.sonatype.com/artifact/io.github.mavenplugins/unsigner-maven-plugin/1.0)

### Summary
- Initial release of this artifact with new groupId `io.github.mavenplugins`
- Codewise identical with `org.netxms:unsigner-maven-plugin:1.0`<br>
  No more features nor changes
- Released to Maven Central

### Updates
- pom.xml:
  - update parent pom reference
  - update groupId to io.github.mavenplugins
  - update URLs to fit with new repo location

- README.md:
  - add URLs for build tags
  - update URLs of fork reference
  - update Maven POM examples


<!--
## []

### NeverReleased
- This is just a dummy placeholder to make the parser of GHCICD/release-notes-from-changelog@v1 happy!
-->

[Unreleased]: https://github.com/mavenplugins/unsigner-maven-plugin/compare/v1.0..HEAD
[1.0]: https://github.com/mavenplugins/unsigner-maven-plugin/releases/tag/v1.0
