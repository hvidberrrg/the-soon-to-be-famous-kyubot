var scrollHeight = 10000;
var conversation = $('<div class="conversation">\n' +
    '        <form class="chat" action="/conversation" method="post" id="kyubotQuery">\n' +
    '            <div class="messages" style="height:' + scrollHeight + 'px;">&nbsp;</div>\n' +
    '            <input type="text" id="inputText" name="input" placeholder="Enter your message here...">\n' +
    '            <input type="hidden" id="watson_context" name="context" value="">\n' +
    '            <input type="submit" value="Send">\n' +
    '        </form>\n' +
    '    </div>\n' +
    '    \n' +
    '    <!-- Overlaid content goes here -->\n' +
    '    <div id="overlay">\n' +
    '        <div id="overlay_content_box">\n' +
    '            <button id="overlay_close">×</button>\n' +
    '            <span id="overlay_content"><!-- external content to be loaded here --></span>\n' +
    '        </div>\n' +
    '    </div>');

var fixtures = new jasmine.getFixtures();