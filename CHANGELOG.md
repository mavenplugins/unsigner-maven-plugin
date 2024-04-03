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


## [1.1]
<!-- !!! Align version in badge URLs as well !!! -->
[![1.1 Badge](https://img.shields.io/nexus/r/io.github.mavenplugins/unsigner-maven-plugin?server=https://s01.oss.sonatype.org&label=Maven%20Central&queryOpt=:v=1.1)](https://central.sonatype.com/artifact/io.github.mavenplugins/unsigner-maven-plugin/1.1)

### Summary
- Use `JarSignerUtil` of `org.apache.maven.shared:maven-jarsigner` for unsigning
- Fix -D property examples in README.md
- Fix to unsign project main artifact by default
- Add configuration item `processMainArtifact` to disable unsigning the project main artifact
- Add help descriptions to Mojo parameters

### Fixes
- README.md:
  - Fix -D property examples

- UnsignGoal.java:
  - Fix to unsign project main artifact by default

### Changes
- pom.xml:
  - add compile dependency to `org.apache.maven.shared:maven-jarsigner:3.0.0`

- UnsignGoal.java:
  - replace usage of `Unsigner.unsign(...)` by `JarSignerUtil.unsignArchive(archiveFile)`
  - improve check of artifact File before calling `JarSignerUtil.unsignArchive(archiveFile)`
  - add configuration item `processMainArtifact` (default=true)
  - add info log in case artifact ID is filtered
  - add help descriptions to Mojo parameters
  
- Unsigner.java:
  - remove since this class is obsolete now
  
- README.md:
  - add description for config item `processMainArtifact`


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

[Unreleased]: https://github.com/mavenplugins/unsigner-maven-plugin/compare/v1.1..HEAD
[1.1]: https://github.com/mavenplugins/unsigner-maven-plugin/compare/v1.0..v1.1
[1.0]: https://github.com/mavenplugins/unsigner-maven-plugin/releases/tag/v1.0
