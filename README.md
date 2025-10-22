# PSP System
PSP dynamic payment routing app
# Build project
`gradle clean build`
# Build image
`docker build -t psp-system:1.0.0 .`
# Run image
`docker run --name psp-system -p 8080:8080 psp-system:1.0.0 `
