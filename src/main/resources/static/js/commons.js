const DEV = false;

let lang = "en-US"; // "de-DE" "en-US"

let currentUser = null;

let dummyUser = {
    "id": 2420,
    "login": "djordje.gavrilovic",
    "firstName": "Djordje",
    "salutation": "Mr.",
    "lastName": "Gavrilovic",
    "email": "djordje.gavrilovic@uptempo.io",
    "orgUnitName": "Brandmaker",
    "language": "en-US",
    "rights": null,
    "vdbGroupName": null,
    "roles": {
        "PIMEDIA_DATABASE": "administrator",
        "TILE_VIEW": "administrator",
        "DASHBOARD": "administrator",
        "REPORTING_CENTER": "administrator",
        "CI_PORTAL": "Administrator",
        "PIMEDIA_ORGANISATION": "administrator",
        "TABLEAU": "ExplorerCanPublish",
        "PIMEDIA_PUBLICATION": "administrator"
    },
    "organizationalUnitId": 203
}

// Pagination variables
let currentPage = 1;
const itemsPerPage = 3;

// Helper log function
function cLog(msg, obj) {
    let message = "QR-GEN: " + msg;
    if (obj !== undefined) {
        console.log(message,obj);
    } else {
        console.log(message);
    }
}

const MESSAGE = "Commons loaded.";

const translations = {
    en: {
        1: "QR CODE GENERATOR",
        2: "Search",
        3: "Create New QR Code",
        4: "Campaign Name",
        5: "Campaign Medium",
        6: "Language",
        7: "URL Code",
        8: "Additional Information",
        9: "URL Encoded",
        10: "Created",
        11: "Published",
        12: "Tracking URL",
        13: "Previous",
        14: "Page {x} of {y}",
        15: "Next",
        16: "Create",
        17: "Edit QR Code",
        18: "Edit",
        19: "Please confirm delete",
        20: "Delete",
        21: "Download QR Code",
        22: "Download",
        23: "Format",
        24: "Background color",
        25: "Color model",
        26: "Please confirm publish update.",
        27: "Confirm",
        28: "Page",
        29: "of",
        30: "Brochure",
        31: "Postcard",
        32: "Advert",
        33: "Poster",
        34: "Merchandise",
        35: "German",
        36: "English",
        37: "Color"
    },
    de: {
        1: "QR CODE GENERATOR",
        2: "Suche",
        3: "Neuen QR-Code erstellen",
        4: "Kampagnenname",
        5: "Kampagnenmedium",
        6: "Sprache",
        7: "URL-Code",
        8: "Zusätzliche Informationen",
        9: "URL-kodiert",
        10: "Erstellt",
        11: "Veröffentlicht",
        12: "Tracking-URL",
        13: "Zurück",
        14: "Seite {x} von {y}",
        15: "Weiter",
        16: "Erstellen",
        17: "QR-Code bearbeiten",
        18: "Bearbeiten",
        19: "Bitte Löschung bestätigen",
        20: "Löschen",
        21: "QR-Code herunterladen",
        22: "Herunterladen",
        23: "Format",
        24: "Hintergrundfarbe",
        25: "Farbmodell",
        26: "Bitte Veröffentlichungs-Update bestätigen.",
        27: "Bestätigen",
        28: "Seite",
        29: "von",
        30: "Broschüre",
        31: "Postkarte",
        32: "Anzeige",
        33: "Plakat",
        34: "Werbeartikel",
        35: "Deutsch",
        36: "Englisch",
        37: "Farbe"
    }
};
