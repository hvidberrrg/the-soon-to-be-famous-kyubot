var kyubot = new Kyubot();

function initKyubot() {
    // Add handler for submitting messages to the KyuBOT...
    $("#kyubotQuery").submit(function (event) {
        kyubot.submitQueryToWatson($(this), event);
    });

    // Add event handlers needed for overlaid content
    addOverlayHandlers();

    // ... and begin the conversation
    kyubot.initiateConversation();
}

function addOverlayHandlers() {
    // Overlays can contain overlay links themselves, i.e. we need to support
    // event binding on dynamically created elements in this case.
    $(document).on('click', "a", function(e) {
        kyubot.displayOverlay(e, $(this).attr('href'));
    });

    // Close overlay on click outside of the overlay content
    $("#overlay").on('click', function(e){
        if (e.target !== this)
            return;
        else{
            kyubot.hideOverlay();
        }
    });

    // Close overlay button
    $("#overlay_close").on('click', function(){
        kyubot.hideOverlay();
    });
}

// Start the conversation as soon as the document is ready
$(document).ready(initKyubot);
