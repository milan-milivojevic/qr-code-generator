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