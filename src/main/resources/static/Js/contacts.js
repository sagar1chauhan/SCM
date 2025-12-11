console.log("Contacts.js");
const baseURL ="http://localhost:8081";
// Wait until Flowbite and DOM are both ready
document.addEventListener("DOMContentLoaded", () => {
  const viewContactModal = document.getElementById("view_contact_modal");

  if (!viewContactModal) {
    console.error("Modal element not found: #view_contact_modal");
    return;
  }

  // ✅ Ensure Flowbite Modal class is available
  const Modal = window.Flowbite?.Modal || window.Modal;
  if (!Modal) {
    console.error("Flowbite Modal class not found. Check Flowbite JS import order.");
    return;
  }

  const options = {
    placement: "bottom-right",
    backdrop: "dynamic",
    backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
    closable: true,
    onHide: () => console.log("modal is hidden"),
    onShow: () => console.log("modal is shown"),
    onToggle: () => console.log("modal has been toggled"),
  };

  const instanceOptions = {
    id: "view_contact_modal",
    override: true,
  };

  const contactModal = new Modal(viewContactModal, options, instanceOptions);

  // Attach globally so Thymeleaf inline onclick can access it
  window.openContactModal = () => contactModal.show();
  window.closeContactModal = () => contactModal.hide();

  window.loadContactdata = async (id) => {
    console.log("Fetching contact:", id);
    try {
      const response = await fetch(`${baseURL}/api/contacts/${id}`);
      const data = await response.json();

      console.log(data);
      document.querySelector("#contact_name").innerHTML = data.name;
      document.querySelector("#contact_email").innerHTML = data.email;
      document.querySelector("#contact_image").src = data.picture;
      document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
      document.querySelector("#contact_address").innerHTML = data.address;
      document.querySelector("#contact_about").innerHTML = data.description;

      const contactFavorite = document.querySelector("#contact_favorite");
      if (data.favorite) {
        contactFavorite.innerHTML =
          "<i class='fas fa-star text-yellow-400'></i>".repeat(4);
      } else {
        contactFavorite.innerHTML = "Not Favorite Contact";
      }

      document.querySelector("#contact_website").href = data.websiteLink;
      document.querySelector("#contact_website").innerHTML = data.websiteLink;
      document.querySelector("#contact_linkedIn").href = data.linkedInLink;
      document.querySelector("#contact_linkedIn").innerHTML = data.linkedInLink;

      openContactModal();
    } catch (error) {
      console.error("Error loading contact data:", error);
    }
  };
});
//delete contact function 
async function deleteContact(id){
Swal.fire({
  title: "Do you want to delete the contact?",
  icon:"warning",
  showCancelButton: true,
  confirmButtonText: "Save",

}).then((result) => {
  /* Read more about isConfirmed, isDenied below */
  if (result.isConfirmed) {
    const url = `${baseURL}/user/contacts/delete/`+id;
    window.location.replace(url);
  } 
});
}