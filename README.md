# Jar Unsigner plugin for Apache Maven
[![Apache License](https://img.shields.io/github/license/mavenplugins/unsigner-maven-plugin?label=License)](./LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.mavenplugins/unsigner-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.mavenplugins/unsigner-maven-plugin)
[![CI](https://github.com/mavenplugins/unsigner-maven-plugin/actions/workflows/build_and_deploy.yml/badge.svg)](https://github.com/mavenplugins/unsigner-maven-plugin/actions/workflows/build_and_deploy.yml)

Forked from [netxms/unsigner-maven-plugin](https://github.com/netxms/maven-unsigner-plugin).

This is a very simple plugin whose only function is to strip signature files out of jars. In addition to the Maven plugin interface, it has a main class, to allow manual processing of jars. The plugin interface can process both the project's main artifact and its attached artifacts (when configured). This is particularly useful when aggregation of dependencies into a project's own jar inadvertently brings in signature files from the dependency. One notable case is in the Apache Felix `maven-bundle-plugin`, when the `<Embed-Dependency/>` configuration is used and a dependency has been signed. When a signed dependency's signature files are included in your project's jar, those signature files will not include checksums for your own classes, which will render your artifact unusable.

## Basic Usage

```xml
<plugin>
    <groupId>io.github.mavenplugins</groupId>
    <artifactId>unsigner-maven-plugin</artifactId>
    <version>1.1</version>
    <executions>
        <execution>
            <id>unsign-jar</id>
            <goals>
                <goal>unsign</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Skipping by Configuration

If you've configured the unsign plugin to run in a parent POM, and you need to disable this function for a given child project, you can provide the following configuration:

```xml
<plugin>
    <groupId>io.github.mavenplugins</groupId>
    <artifactId>unsigner-maven-plugin</artifactId>
    <configuration>
        <skip>true</skip>
    </configuration>
</plugin>
```

## Skipping from the Command Line

On the other hand, if you need to skip unsigning from the command line (usually to help in debugging a build, **NOT** to be used as a permanent part of your build!), you can use the following:

```sh
mvn -Dunsign.skip=true ...
```

## Processing Project Attachments

If you want to unsign your project's attached artifacts in addition to the main artifact, you can use the following plugin configuration:

```xml
<plugin>
    <groupId>io.github.mavenplugins</groupId>
    <artifactId>unsigner-maven-plugin</artifactId>
    <configuration>
        <processAttachments>true</processAttachments>
    </configuration>
</plugin>
```

Alternatively, for debugging purposes, you could use the command line parameter:

```sh
mvn -Dunsigner.processAttachments=true ...
```

## Disable Processing of the Project Main Artifact

If you do not want to unsign your project's main artifact, you can use the following plugin configuration:

```xml
<plugin>
    <groupId>io.github.mavenplugins</groupId>
    <artifactId>unsigner-maven-plugin</artifactId>
    <configuration>
        <processMainArtifact>false</processMainArtifact>
    </configuration>
</plugin>
```

Alternatively, for debugging purposes, you could use the command line parameter:

```sh
mvn -Dunsigner.processMainArtifact=false ...
```