const toggleSwitch = document.querySelector('#theme-checkbox');
const currentTheme = localStorage.getItem('theme');

// Check for saved user preference
if (currentTheme) {
    document.body.classList.add(currentTheme);
    if (currentTheme === 'dark') {
        toggleSwitch.checked = true;
    }
}

toggleSwitch.addEventListener('change', (e) => {
    if (e.target.checked) {
        document.body.classList.add('dark');
        localStorage.setItem('theme', 'dark');
        // Optional: Update cookie for JSP/Servlets
        document.cookie = "theme_pref=dark; path=/";
    } else {
        document.body.classList.remove('dark');
        localStorage.setItem('theme', 'light');
        document.cookie = "theme_pref=light; path=/";
    }
});