
# QR Code Generator & address forwarding extension

---

**Confluence:** 

https://jira-brandmaker.atlassian.net/wiki/spaces/PSBEI/pages/845938729/QR-Code+generator+and+address+forwarding+Extension

**Grafana:**
https://observability.adm.brandmaker.net/explore?orgId=1&left=%7B%22datasource%22:%22Loki%22,%22queries%22:%5B%7B%22refId%22:%22A%22,%22expr%22:%22%7Bapp%3D%5C%22qr-code-generator%5C%22%7D%22%7D%5D,%22range%22:%7B%22from%22:%22now-1h%22,%22to%22:%22now%22%7D%7D
---

**Run locally:**

- In application.properties set desired active profile
- In commons.js set DEV = true
- rename logback-spring.xml

---

TODO:

1. User permission
