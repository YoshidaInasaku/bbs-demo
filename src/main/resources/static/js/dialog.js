function modal_open() {
    const dialog = document.getElementById("register-modal");
    dialog.style.left = ((window.innerWidth - 500) / 2) + "px";
    dialog.style.display = "flex";

    const overlay = document.getElementById("modal-overlay");
    overlay.style.display = "block";
}

function modal_close() {
    document.getElementById("register-modal").style.display = "none";
    document.getElementById("modal-overlay").style.display = "none";
}