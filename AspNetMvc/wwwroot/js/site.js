// Please see documentation at https://docs.microsoft.com/aspnet/core/client-side/bundling-and-minification
// for details on configuring this project to bundle and minify static web assets.
// Write your JavaScript code.



/*
 revealing starrs by percent
*/

$(document).ready(function () {
    // Gets the span width of the filled-ratings span
    // this will be the same for each rating
    var star_rating_width = $('.fill-ratings span').width();
    // Sets the container of the ratings to span width
    // thus the percentages in mobile will never be wrong
    $('.star-ratings').width(star_rating_width);
});

/*
$(function () {
    var connection = new signalR.HubConnectionBuilder().withUrl("/myHub").build();

    connection.start();

    $('textarea').keyup(() => {
        //activate in the server the method called changed in MyHub.cs
        const v = $('textarea').val();
        console.log(v);
        connection.invoke("Changed", v);
    });

    connection.on("ChangeReceived", function (value) {
        $('textarea').val(value);
    });
});*/
