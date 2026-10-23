Hello and welcome, new contributor.

# Issues

Please describe your issue as precisely as possible.

# Pull requests

- Create a branch from `main` with a reasonable name.
- Make your changes and run `./gradlew spotlessApply build`.
- Open a pull request against `main` and describe what you changed and why.
- Keep an eye on the checks and on review comments.

Thank you for your contribution!

# Versions and releases

[publishdata](https://github.com/rainbowdashlabs/publishdata) sets the version. The base version lives in
`gradle.properties` and nowhere else.

| Build | Version | Published to |
|---|---|---|
| `main` | `<version>-SNAPSHOT` | EldoNexus snapshots |
| tag `vX.Y.Z` | `X.Y.Z` | Maven Central and EldoNexus releases |
| local | `<version>-SNAPSHOT` | nothing |

`./gradlew -q :printVersion` shows the version of the current build.

To release:

1. Make sure `version` in `gradle.properties` on `main` is the version you release.
2. Tag that commit with `vX.Y.Z` and push the tag.
3. `publish.yml` verifies the tag and publishes it. Release the uploaded deployment in the Central Portal.
4. Write the GitHub release notes for the tag.
5. Raise `version` in `gradle.properties` to the next planned release.
