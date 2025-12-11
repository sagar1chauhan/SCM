console.log("adminjs");


document.querySelector("#image_file_input").addEventListener('change',function(event){
    let file = event.target.files[0];
    let reader = new FileReader();
    reader.onload = function(e){
        document.querySelector("#upload_image_preview")
        .setAttribute("src",e.target.result);

    };
    reader.readAsDataURL(file);


});
