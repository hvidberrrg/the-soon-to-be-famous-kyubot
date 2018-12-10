// Conversation endpoint to mock
var conversationUrl = "/conversation";
// Watson context for the conversation
var kyubotContext = {"test": "test context"};
// Reply that must be added to the conversation
var kyubotReply = "KyuBOT reply";
// Full response
var kyubotResponse = {"output": {"text": [kyubotReply]}, "context": kyubotContext};

function installAjaxMock() {
    jasmine.Ajax.install();

    var conversationRegExp = new RegExp(".*\\" + conversationUrl + ".*", "g");
    jasmine.Ajax.stubRequest(conversationRegExp, /.*/, "POST").andReturn({
        "status": 200,
        "contentType": "application/json",
        "responseText": JSON.stringify(kyubotResponse)
    });
}

function uninstallAjaxMock() {
    jasmine.Ajax.uninstall();
}