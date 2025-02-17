# veilarbvedtakinfo

Applikasjon for lagring av informasjon relatert til paragraf §14a vedtak

Veilarbvedtakinfo holder tidspunkt på tidligere besvarelser på start-samtale
- start-samtale gjør POST /api/motestotte ved submit på en start-samtale besvarelse
- aia-backend gjør sannsynligvis GET /api/motestotte, akkurat hvor og når den gjøres må evt sjekkes opp med team-paw (evt sjekkes koden deres selv)

## Innkommende kommuniksjon (inbound communication)
| Collaborator  | Query/Command/Event | Melding         |
|---------------|---------------------|-----------------|
| aia-backend   | query (REST/GET)    | /api/motestotte |
| start-samtale | command (REST/POST) | /api/motestotte |
| flere??       | query (REST/??)     | /?              |