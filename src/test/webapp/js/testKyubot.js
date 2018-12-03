describe('KyuBOT Jasmine unit tests', function(){
    var scrollHeight;

    beforeEach(function() {
        scrollHeight = 10000;
        // add an element to render into
        testElement = $('<div class="conversation">\n' +
            '        <form class="chat" action="/conversation" method="post" id="kyubotQuery">\n' +
            '            <div class="messages" style="height:' + scrollHeight + 'px;">&nbsp;</div>\n' +
            '            <input type="text" id="inputText" name="input" placeholder="Enter your message here...">\n' +
            '            <input type="hidden" id="watson_context" name="context" value="">\n' +
            '            <input type="submit" value="Send">\n' +
            '        </form>\n' +
            '    </div>');

        $('body').append(testElement);
    });

    it('scroll to bottom', function() {
        // Don't use '$(".messages")' for spying. It is not an existing object, but the  result of a jQuery selector
        spyOn($.fn, "animate");
        scrollToBottom();

        var scrollDiv = $(".messages");
        var scrollTopArg = {scrollTop: scrollHeight}
        expect(scrollDiv.animate).toHaveBeenCalledWith(scrollTopArg, 1000);
    })

});