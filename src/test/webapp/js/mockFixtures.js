var scrollHeight = 10000;
var conversation = $('<div class="conversation">\n' +
    '        <form class="chat" action="/conversation" method="post" id="kyubotQuery">\n' +
    '            <div class="messages" style="height:' + scrollHeight + 'px;">&nbsp;</div>\n' +
    '            <input type="text" id="inputText" name="input" placeholder="Enter your message here...">\n' +
    '            <input type="hidden" id="watson_context" name="context" value="">\n' +
    '            <input type="submit" value="Send">\n' +
    '        </form>\n' +
    '    </div>');

var fixtures = new jasmine.getFixtures();