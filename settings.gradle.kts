plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "XaeroMerge"

include("jm-to-xaero")
project(":jm-to-xaero").projectDir = file("JMtoXaero")
