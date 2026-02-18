const form = document.getElementById('signup-form');
const firstName = document.getElementById('firstName');
const lastName = document.getElementById('lastName');
const aadhar = document.getElementById('aadharNumber');
const email = document.getElementById('email');
const password = document.getElementById('password');

// --- 1. Utility Functions ---

function setError(input, message) {
    const formControl = input.parentElement;
    const small = formControl.querySelector('small');
    formControl.className = 'form-control error';
    if (small) small.innerText = message;
}

function setSuccess(input) {
    const formControl = input.parentElement;
    formControl.className = 'form-control success';
}

function setNeutral(input) {
    const formControl = input.parentElement;
    formControl.className = 'form-control';
}

// --- 2. Validation Core Logic ---

function validateInput(input) {
    const value = input.value.trim();

    // Check First Name
    if (input === firstName) {
        if (value === '') {
            setError(firstName, 'First name is required');
            return false;
        } else {
            setSuccess(firstName);
            return true;
        }
    }

    // Check Last Name
    if (input === lastName) {
        if (value === '') {
            setError(lastName, 'Last name is required');
            return false;
        } else {
            setSuccess(lastName);
            return true;
        }
    }

    // Check Aadhar (Must be exactly 12 digits)
    if (input === aadhar) {
        if (value.length !== 12 || isNaN(value)) {
            setError(aadhar, 'Enter a valid 12-digit Aadhar');
            return false;
        } else {
            setSuccess(aadhar);
            return true;
        }
    }

    // Check Password (Min 10 characters)
    if (input === password) {
        if (value.length < 10) {
            setError(password, 'Min. 10 characters required');
            return false;
        } else {
            setSuccess(password);
            return true;
        }
    }

    // Email Validation (Simple check)
    if (input === email) {
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(value)) {
            setError(email, 'Enter a valid email address');
            return false;
        } else {
            setSuccess(email);
            return true;
        }
    }

    // Default for fields like Last Name (if optional)
    if (value !== "") {
        setSuccess(input);
        return true;
    }

    return true;
}

// --- 3. Event Listeners ---

// Real-time feedback as the user types
[firstName, lastName, aadhar, email, password].forEach(input => {
    input.addEventListener('input', () => {
        if (input.value.trim() === "") {
            setNeutral(input); // Remove red/green if they clear the field
        } else {
            validateInput(input); // Validate the specific rule
        }
    });
});

// Final Form Submit Check
form.addEventListener('submit', (e) => {
    let isFirstNameValid = validateInput(firstName);
    let isLastNameValid = validateInput(lastName);
    let isAadharValid = validateInput(aadhar);
    let isEmailValid = validateInput(email);
    let isPasswordValid = validateInput(password);

    if (!(isFirstNameValid && isLastNameValid && isAadharValid && isEmailValid && isPasswordValid)) {
        e.preventDefault(); // Stop submission if any field fails
    }
});