console.log("script loaded");

let currentTheme = getTheme();

// Apply initial theme
changePageTheme(currentTheme, null);

// Attach event listener
const changeThemeButton = document.querySelector("#theme_change_button");
changeThemeButton.addEventListener("click", () => {
  console.log("change theme button clicked");

  let oldTheme = currentTheme;

  // Toggle theme
  currentTheme = currentTheme === "dark" ? "light" : "dark";

  // Apply new theme
  changePageTheme(currentTheme, oldTheme);
});

// Set theme to localStorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// Get theme from localStorage
function getTheme() {
  let theme = localStorage.getItem("theme");
  return theme ? theme : "light";
} 

// Change current page theme
function changePageTheme(theme, oldTheme) {
  // Save theme
  setTheme(theme);

  const htmlEl = document.querySelector("html");

  // Remove old theme if exists
  if (oldTheme) {
    htmlEl.classList.remove(oldTheme);
  }

  // Add new theme
  htmlEl.classList.add(theme);

  // Update button text
  document.querySelector("#theme_change_button span").textContent =
    theme === "light" ? "Dark" : "Light";
}
