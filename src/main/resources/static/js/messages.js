function showMessage(elementId, text, type) {
    const element = document.getElementById(elementId);
    const oldType = element.dataset.alertType;

    if (oldType) {
        element.classList.remove(`alert-${oldType}`);
    }

    element.classList.add('alert', `alert-${type}`);
    element.classList.remove('d-none');

    element.dataset.alertType = type;
    element.textContent = text;
}

function hideMessage(elementId){
    const element = document.getElementById(elementId);
    element.textContent = '';
    element.classList.add('d-none');
}

const AlertType = Object.freeze({
    SUCCESS: 'success',
    WARNING: 'warning',
    DANGER: 'danger'
});