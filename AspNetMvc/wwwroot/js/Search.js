$(function () {
    
    $('form').submit(e => {
     
         e.preventDefault();
        const s = $('#Search').val();
      
         $('tbody').load('/Ratings/SearchPart?query='+s)
     })


    
});

function SubmitForm(e) {
    e.submit;
}