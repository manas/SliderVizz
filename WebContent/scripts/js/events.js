/****Copyright (c) <2012> <Manas Agrawal>.
All rights reserved.

Redistribution and use in source and binary forms are permitted
provided that the above copyright notice and this paragraph are
duplicated in all such forms and that any documentation,
advertising materials, and other materials related to such
distribution and use acknowledge that the software was developed
by the <organization>.  The name of the
University may not be used to endorse or promote products derived
from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.*****/

(function($) {
  $.print = function( message, insertType ) {
    insertType = insertType || "append";
    if ( typeof(message) == "object" ) {
      var string = "{<br>",
          values = [],
          counter = 0;
      $.each( message, function( key, value ) {
        if ( value && value.nodeName ) {
          var domnode = "&lt;" + value.nodeName.toLowerCase();
          domnode += value.className ? " class='" + value.className + "'" : "";
          domnode += value.id ? " id='" + value.id + "'" : "";
          domnode += "&gt;";
          value = domnode;
        }
        values[counter++] = key + ": " + value;
      });
      string += values.join( ",<br>" );
      string += "<br>}";
      message = string;
    }

    var $output = $( "#print-output" );

    if ( !$output.length ) {
      $output = $( "<div id='print-output' />" ).appendTo( "body" );
    }

    var newMsg = $('<div />', {
      "class": "print-output-line",
      html: message
    });

    $output[insertType]( newMsg );
  };
})(jQuery);
