# PSP System
PSP dynamic payment routing app
# Build project
`gradle clean build`
# Build image
`docker build -t psp-system:1.0.0 .`
# Run image
`docker run --name psp-system -p 8080:8080 psp-system:1.0.0 `

<!-- coverage-badge-start -->
![Coverage](https://github.com/samandarjon/psp-system/raw/feat/gha/.github/badges/jacoco.svg) ![Branches](https://github.com/samandarjon/psp-system/raw/feat/gha/.github/badges/branches.svg)
<!-- coverage-badge-end -->
