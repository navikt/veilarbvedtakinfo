FROM ghcr.io/navikt/poao-baseimages/java:21
COPY /target/veilarbvedtakinfo.jar app.jar
