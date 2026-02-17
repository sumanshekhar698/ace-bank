const form = document.getElementById('signup-form');
const firstName = document.getElementById('firstName');
const lastName = document.getElementById('lastName');
const aadhar = document.getElementById('aadharNumber');
const email = document.getElementById('email');
const password = document.getElementById('password');

// Utility function to show error
function setError(input, message) {
    const formControl = input.parentElement;
    const small = formControl.querySelector('small');
    formControl.className = 'form-control error';
    small.innerText = message;
}

// Utility function to show success
function setSuccess(input) {
    const formControl = input.parentElement;
    formControl.className = 'form-control success';
}

// Validation Logic
function checkInputs() {
    let isValid = true;

    if (firstName.value.trim() === '') {
        setError(firstName, 'First name is required');
        isValid = false;
    } else setSuccess(firstName);

    if (aadhar.value.length !== 12 || isNaN(aadhar.value)) {
        setError(aadhar, 'Enter a valid 12-digit Aadhar');
        isValid = false;
    } else setSuccess(aadhar);

    if (password.value.length < 10) {
        setError(password, 'Min. 10 characters required');
        isValid = false;
    } else setSuccess(password);

    return isValid;
}

// Event Listener for Real-time feedback
[firstName, lastName, aadhar, email, password].forEach(input => {
    input.addEventListener('input', () => {
        if (input.value.trim() !== "") setSuccess(input);
    });
});

// Final Form Submit Check
form.addEventListener('submit', (e) => {
    if (!checkInputs()) {
        e.preventDefault(); // Stop form from sending if invalid
    }
});