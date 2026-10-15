function clearValidationErrors(fields) {
    for (const [field, inputId] of Object.entries(fields)) {
        document.getElementById(`${inputId}Error`).textContent = '';
        document.getElementById(inputId).classList.remove('is-invalid');
    }
}

function showValidationErrors(errors, fields) {
    for (const [field, message] of Object.entries(errors)) {
        const inputId = fields[field];
        if (!inputId) continue;

        document.getElementById(`${inputId}Error`).textContent = message;
        document.getElementById(inputId).classList.add('is-invalid');
    }
}