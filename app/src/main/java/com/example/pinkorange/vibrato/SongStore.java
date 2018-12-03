package com.example.pinkorange.vibrato;

import java.util.ArrayList;

class SongStore {
    private ArrayList<Song> songs;

    SongStore() {
        this.songs = new ArrayList<>();
        this.songs.add(new Song(
                R.raw.croatian,
                "Croatian Rhapsody",
                "Maksim Mrvica"
        ));
        this.songs.add(new Song(
                R.raw.highscore,
                "High Score",
                "Panda Eyes"
        ));
        this.songs.add(new Song(
                R.raw.unity,
                "Unity",
                "The Fat Rat"
        ));
        this.songs.add(new Song(
                R.raw.christmas,
                "All I Want For Christmas Is You",
                "Mariah Carey",
                "There's just one thing I need\n" +
                    "I don't care about the presents\n" +
                    "Underneath the Christmas tree\n" +
                    "I just want you for my own\n" +
                    "More than you could ever know\n" +
                    "Make my wish come true\n" +
                    "All I want for Christmas is\n" +
                    "You\n\n" +
                    "I don't want a lot for Christmas\n" +
                    "There is just one thing I need\n" +
                    "I don't care about the presents\n" +
                    "Underneath the Christmas tree\n" +
                    "I don't need to hang my stocking\n" +
                    "There under the fireplace\n" +
                    "Santa Claus won't make me happy\n" +
                    "With a toy on Christmas day\n" +
                    "I just want you for my own\n" +
                    "More than you could ever know\n" +
                    "Make my wish come true\n" +
                    "'Cause all I want for Christmas is you\n\n" +
                    "I won't ask for much this Christmas\n" +
                    "I don't even wish for snow\n" +
                    "I'm just gonna keep on waiting\n" +
                    "Underneath the mistletoe\n" +
                    "I won't make a list and send it\n" +
                    "To the North Pole for Saint Nick\n" +
                    "I won't even stay awake to\n" +
                    "Hear those magic reindeers click\n" +
                    "'Cause I just want you here tonight\n" +
                    "Holding on to me so tight\n" +
                    "What more can I do\n" +
                    "All I want for Christmas is you\n" +
                    "Ooh baby\n\n" +
                    "All the lights are shining\n" +
                    "So brightly everywhere\n" +
                    "And the sound of children's\n" +
                    "Laughter fills the air\n" +
                    "And everyone is singing\n" +
                    "I hear those sleigh bells ringing\n" +
                    "Santa won't you bring me the one I really need\n" +
                    "Won't you please bring my baby to me\n\n" +
                    "Oh I don't want a lot for Christmas\n" +
                    "This is all I'm asking for\n" +
                    "I just want to see my baby\n" +
                    "Standing right outside my door\n" +
                    "Oh I just want you for my own\n" +
                    "More than you could ever know\n" +
                    "Make my wish come true\n" +
                    "Baby all I want for Christmas is\n" +
                    "You\n\n" +
                    "All I want yeah you, yeah you \n" +
                    "All I want for Christmas is you"
        ));
        this.songs.add(new Song(
                R.raw.grilboyfriend,
                "Girlfriend Boyfriend",
                "Blackstreet",
                "Blackstreet, JJ\nCome on\nUh, uh, uh, uh" +
                        "\nYeah, what what\n\n" +
                        "What's up girlfriend?\n" +
                        "What\'s up boyfriend?\nYo, meet my girlfriend \n" +
                        "Meet my boyfriend \n" +
                        "This is my girlfriend \n" +
                        "This is my boyfriend \n" +
                        "So what's up girlfriend? \n" +
                        "So what's up boyfriend? \n" +
                        "Yeah you know, uh huh, what's up?\n\n" +
                        "I can't get her off my back \n" +
                        "Gave her a little love, she don't know how to act \n" +
                        "She be gettin' mad 'cause I don't love her back \n" +
                        "I didn't know honey gets down like that \n" +
                        "Now fella gotta watch his back \n" +
                        "This female is a fatal attract' \n" +
                        "Maybe 'cause she got zipper to jack \n" +
                        "She didn't know I puts it down like that, that's why\n\n" +
                        "1 Girlfriend on the phone \n" +
                        "Call me all day on the telephone \n" +
                        "Blowin' up my beeper 'cause she ready to bone \n" +
                        "Played me once, won't leave me alone\n\n" +
                        "She keep paging me, calling me, stalking me, hawkin' me \n" +
                        "Followin' me, telling me that she lovin' me \n" +
                        "But my girlfriend said, just handle it \n" +
                        "I can't handle it \n" +
                        "Just handle it \n" +
                        "I can't handle it \n" +
                        "Just handle it \n" +
                        "I can't handle it \n" +
                        "Uh, pick it up JJ one time\n\n" +
                        "I can't get him out of my hair \n" +
                        "Had the boy playin' truth or dare \n" +
                        "Callin' my phone at to where I be \n" +
                        "Boy said, sweetie you're my main squeeze \n" +
                        "It's 2 AM and he's back again \n" +
                        "Arms on his waist, all in my way \n" +
                        "Plottin' ways to get in my mix \n" +
                        "Boy, already there's a little spice in this\n\n" +
                        "2- Boyfriend on the phone \n" +
                        "You call me all day on the telephone \n" +
                        "Blowin' up my pager 'cause he ready to bone \n" +
                        "Played me once, won't leave me alone\n\n" +
                        "He keep paging me, calling me, stalking me, hawkin' me \n" +
                        "Followin' me, telling me that he's lovin' me \n" +
                        "But my baby said, just handle it \n" +
                        "I can't handle it \n" +
                        "Just handle it \n" +
                        "I can't handle it \n" +
                        "Just handle it \n" +
                        "I can't handle it \n" +
                        "Just handle it \n" +
                        "I can't handle it\n\n" +
                        "Uh, uh, yeah, turn the lights off, it's about to get plenty dark \n" +
                        "You wasn't smart, you started f-' Ja with your heart \n" +
                        "If I ripped it apart don't hate me, thank me baby \n" +
                        "If my world was yours it would drive you crazy \n" +
                        "'cause I love what I do, like f-' you hoes and soon \n" +
                        "Talk to your tears until you feel there's something to prove \n" +
                        "And with nothing to lose I can see you being a tease \n" +
                        "You f-' with me, just know we f-' for free\n\n" +
                        "Yeah I know that you was lost, first bite had you tossed \n" +
                        "E-V-E, caramel skin b- cost \n" +
                        "And before you stroke the kitty n- better break off \n" +
                        "N- want to f-' run, better shake off \n" +
                        "Show me something, diamonds and the furs ain't nothin' \n" +
                        "Impress me, bless me with a Hummer, think I'm frontin'? \n" +
                        "Big cat with the big gat ready to f- \n" +
                        "One nut you done screamin' damn baby I'm stuck\n\n" +
                        "Why in the world would you continue to run my way? \n" +
                        "Got hit once, found out that I don't play \n" +
                        "What the deal mami? \n" +
                        "Who pushed you through the irony of \n" +
                        "F-' and suckin' me, splitin' the coke with me\n\n" +
                        "Yeah you use to have me flippin' \n" +
                        "All your ex-hoes had me b-' \n" +
                        "Daddy, I never front, your d- game keep me twitchin' (no doubt) \n" +
                        "How can you deny this freak? \n" +
                        "Shhh, no need to speak, just meet me on Blackstreet\n\n" +
                        "Everybody now\n\n" +
                        "Boy/Girlfriend on the phone \n" +
                        "Call me all day on the telephone \n" +
                        "Blowin' up my pager 'cause s/he ready to bone \n" +
                        "Played me once, won't leave me alone\n\n" +
                        "S/he keep paging me, calling me, stalking me, hawkin' me \n" +
                        "Followin' me, telling me that he lovin' me \n" +
                        "But my baby/girlfriend said, just handle it \n" +
                        "I can't handle it \n" +
                        "Just handle it \n" +
                        "I can't handle it \n" +
                        "Just handle it \n" +
                        "I can't handle it \n" +
                        "Just handle it \n" +
                        "I can't handle it\n\n" +
                        "We out, we out"
        ));
        this.songs.add(new Song(
                R.raw.forever,
                "Forever",
                "Chris Brown",
                "One, two, three, four\n" +
                        "Hey, hey, forever\n" +
                        "Hey, hey, forever\n\n" +
                        "It's you, and me\n" +
                        "Movin' at the speed of light into eternity, yeah\n" +
                        "Tonight, is the night\n" +
                        "To join me in the middle of ecstasy\n" +
                        "Feel the melody and the rhythm of the music around you, around you\n\n" +
                        "I'ma take you there, I'ma take you there\n" +
                        "So don't be scared, I'm right here, baby\n" +
                        "We can go anywhere, go anywhere\n" +
                        "But first, it's your chance, take my hand, come with me\n\n" +
                        "It's like I waited my whole life\n" +
                        "For this one night\n" +
                        "It's gon' be me you and the dance floor\n" +
                        "'Cause we only got one night\n" +
                        "Double your pleasure\n" +
                        "Double your fun\n" +
                        "And dance forever ever ever\n" +
                        "Forever, ever, ever\n" +
                        "Forever, ever, ever\n" +
                        "Forever (forever)\n" +
                        "Forever, ever, ever\n" +
                        "Forever, ever, ever\n" +
                        "Forever, ever, ever\n" +
                        "Forever on the dance floor\n\n" +
                        "Feels like we're on another level\n" +
                        "Feels like our love's intertwined\n" +
                        "We can be two rebels\n" +
                        "Breaking the rules, me and you, you and I\n" +
                        "All you gotta do is watch me (watch me)\n" +
                        "Look what I can do with my feet (my feet)\n" +
                        "Baby, feel the beat inside\n" +
                        "I'm driving, you can take the front seat (front seat)\n" +
                        "Just need you to trust me (trust me)\n" +
                        "Oh (girl), ah (girl), ah (girl)\n" +
                        "It's like I've\n\n" +
                        "It's like I waited my whole life\n" +
                        "For this one night (one night)\n" +
                        "It's gon' be me you and the dance floor (dance floor)\n" +
                        "'Cause we only got one night (one night)\n" +
                        "Double your pleasure\n" +
                        "Double your fun\n" +
                        "And dance forever ever ever\n" +
                        "Forever, ever, ever (ever)\n" +
                        "Forever, ever, ever (ever)\n" +
                        "Forever (forever)\n" +
                        "Forever, ever, ever (ever)\n" +
                        "Forever, ever, ever (ever)\n" +
                        "Forever, ever, ever (ever)\n" +
                        "Forever on the dance floor\n\n" +
                        "It's a long way down\n" +
                        "We so high off the ground\n" +
                        "Sendin' for an angel to bring me your heart\n" +
                        "Girl, where did you come from?\n" +
                        "Got me so undone\n" +
                        "Gaze in your eyes got me sayin'\n" +
                        "\"What a beautiful lady\"\n" +
                        "No if, ands or maybes\n" +
                        "I'm releasin' my heart\n" +
                        "And it's feelin' amazing\n" +
                        "There's no one else that matters\n" +
                        "You love me\n" +
                        "And I wont let you fall, girl (fall, girl)\n" +
                        "Let you fall girl, oh\n" +
                        "Oh, oh, yeah\n" +
                        "Yeah, I won't let you fall\n" +
                        "Let you fall\n" +
                        "Let you fall\n" +
                        "Oh, oh\n" +
                        "Yeah, yeah\n" +
                        "Yeah, yeah\n" +
                        "It's like\n\n" +
                        "It's like I waited my whole life (a whole life)\n" +
                        "For this one night (one night)\n" +
                        "It's gon' be me you and the dance floor (me you and the dance floor)\n" +
                        "'Cause we only got one night\n" +
                        "Double your pleasure\n" +
                        "Double your fun\n" +
                        "And dance forever ever ever\n" +
                        "Forever, ever, ever\n" +
                        "Forever, ever, ever\n" +
                        "Forever\n" +
                        "Forever, ever, ever\n" +
                        "Forever, ever, ever\n" +
                        "Forever, ever, ever\n" +
                        "Forever on the dance floor\n\n" +
                        "Oh! Oh! Oh-oh-oh, oh, oh\n" +
                        "Oh, yeah\n" +
                        "Forever, ever, ever, ever\n" +
                        "Forever, ever, oh\n"
        ));
        this.songs.add(new Song(
                R.raw.alreadygone,
                "Already Gone",
                "Kelly Clarkson",
                "Remember all the things we wanted\n" +
                        "Now all our memories, they're haunted\n" +
                        "We were always meant to say goodbye\n" +
                        "Even without fists held high, yeah\n" +
                        "Never would have worked out right, yeah\n" +
                        "We were never meant for do or die\n\n" +
                        "I didn't want us to burn out\n" +
                        "I didn't come here to hurt you now\n" +
                        "I can't stop\n\n" +
                        "I want you to know\n" +
                        "That it doesn't matter\n" +
                        "Where we take this road\n" +
                        "Someone's gotta go\n" +
                        "And I want you to know\n" +
                        "You couldn't have loved me better\n" +
                        "But I want you to move on\n" +
                        "So I'm already gone\n\n" +
                        "Looking at you makes it harder\n" +
                        "But I know that you'll find another\n" +
                        "That doesn't always make you wanna cry\n" +
                        "Started with a perfect kiss\n" +
                        "Then we could feel the poison set in\n" +
                        "Perfect couldn't keep this love alive\n\n" +
                        "You know that I love you so\n" +
                        "I love you enough to let you go\n\n" +
                        "I want you to know\n" +
                        "That it doesn't matter\n" +
                        "Where we take this road\n" +
                        "Someone's gotta go\n" +
                        "And I want you to know\n" +
                        "You couldn't have loved me better\n" +
                        "But I want you to move on\n" +
                        "So I'm already gone\n\n" +
                        "I'm already gone\n" +
                        "I'm already gone\n" +
                        "You can't make it feel right\n" +
                        "When you know that it's wrong\n" +
                        "I'm already gone\n" +
                        "Already gone\n" +
                        "There's no moving on\n" +
                        "So I'm already gone\n\n" +
                        "Already gone\n" +
                        "Already gone\n" +
                        "Already gone\n\n" +
                        "Oh, oh\n\n" +
                        "Already gone\n" +
                        "Already gone\n" +
                        "Already gone\n\n" +
                        "Yeah\n\n" +
                        "Remember all these things we wanted\n" +
                        "Now all our memories, they're haunted\n" +
                        "We were always meant to say goodbye\n\n" +
                        "I want you to know\n" +
                        "That it doesn't matter\n" +
                        "Where we take this road\n" +
                        "Someone's gotta go\n" +
                        "And I want you to know\n" +
                        "You couldn't have loved me better\n" +
                        "But I want you to move on\n" +
                        "So I'm already gone\n\n" +
                        "I'm already gone\n" +
                        "I'm already gone\n" +
                        "You can't make it feel right\n" +
                        "When you know that its wrong\n" +
                        "I'm already gone\n" +
                        "Already gone\n" +
                        "There's no moving on\n" +
                        "So I'm already gone\n"
        ));
        this.songs.add(new Song(
                R.raw.doesntmatter,
                "Doesn't Really Matter",
                "Janet Jackson",
                "Doesn't matter, doesn't matter\n" +
                        "Doesn't matter at all\n" +
                        "\n" +
                        "Doesn't matter what your friends are telling you\n" +
                        "Doesn't matter what my family's saying too\n" +
                        "It just matters that I'm in love with you\n" +
                        "It only matters that you love me too\n" +
                        "It doesn't matter if they won't accept you\n" +
                        "I'm accepting of you and the things you do\n" +
                        "Just as long as it's you\n" +
                        "Nobody but you, baby, baby\n" +
                        "\n" +
                        "My love for you, unconditional love too\n" +
                        "Gotta get up, get up, get up, get up, get up\n" +
                        "And show you that it...\n" +
                        "\n" +
                        "Doesn't really matter what the eye is seeing\n" +
                        "'Cause I'm in love with the inner being\n" +
                        "Doesn't really matter what they believe\n" +
                        "What matters to me is you're in love with me\n" +
                        "Doesn't really matter what the eye is seeing\n" +
                        "'Cause I'm in love with the inner being\n" +
                        "Doesn't really matter what they believe\n" +
                        "What matters to me is you're nutty-nutty-nutty for me\n" +
                        "\n" +
                        "(You're so kind)\n" +
                        "Just what I asked for, you're so loving and kind\n" +
                        "(And you're mine)\n" +
                        "And I can't believe you're mine\n" +
                        "\n" +
                        "Doesn't matter if you're feeling insecure\n" +
                        "Doesn't matter if you're feeling so unsure\n" +
                        "'Cause I'll take away the doubt within' your heart\n" +
                        "And show that my love will never hurt or harm\n" +
                        "Doesn't matter what the pain we go through\n" +
                        "Doesn't matter if the money's gone too\n" +
                        "Just as long as I'm with you\n" +
                        "Nobody but you, baby, baby\n" +
                        "\n" +
                        "Your love for me, unconditional I see\n" +
                        "Gotta get up, get up, get up, get up, get up\n" +
                        "And show you that it\n" +
                        "\n" +
                        "Doesn't really matter what the eye is seeing\n" +
                        "'Cause I'm in love with the inner being\n" +
                        "Doesn't really matter what they believe\n" +
                        "What matters to me is you're in love with me\n" +
                        "Doesn't really matter what the eye is seeing\n" +
                        "'Cause I'm in love with the inner being\n" +
                        "Doesn't really matter what they believe\n" +
                        "What matters to me is you're nutty-nutty-nutty for me\n" +
                        "Doesn't really matter what the eye is seeing\n" +
                        "'Cause I'm in love with the inner being\n" +
                        "Doesn't really matter what they believe\n" +
                        "What matters to me is you're in love with me\n" +
                        "Doesn't really matter what the eye is seeing\n" +
                        "'Cause I'm in love with the inner being\n" +
                        "Doesn't really matter what they believe\n" +
                        "What matters to me is you're nutty-nutty-nutty for me\n" +
                        "\n" +
                        "(You're so kind)\n" +
                        "Oh, just what I asked for, you're so loving and kind\n" +
                        "(And you're mine)\n" +
                        "And I can't believe you're mine\n" +
                        "\n" +
                        "Doesn't matter what they say\n" +
                        "'Cause you know I'm gonna love you anyway\n" +
                        "Doesn't matter what they do\n" +
                        "'Cause my love will always be with you\n" +
                        "\n" +
                        "My love for you, unconditional love too\n" +
                        "Gotta get up, get up, get up, get up, get up\n" +
                        "And show you that my love is true, and it's just for you\n" +
                        "\n" +
                        "Doesn't really matter what the eye is seeing\n" +
                        "'Cause I'm in love with the inner being\n" +
                        "Doesn't really matter what they believe\n" +
                        "What matters to me is you're in love with me\n" +
                        "Doesn't really matter what the eye is seeing\n" +
                        "'Cause I'm in love with the inner being\n" +
                        "Doesn't really matter what they believe\n" +
                        "What matters to me is you're nutty-nutty-nutty for me\n" +
                        "Nutty, nutty, nutty, my love for you\n" +
                        "I can't believe my dreams come true\n" +
                        "I've finally found somebody whose heart is true\n" +
                        "And best of all, you love me too\n" +
                        "And nutty, nutty, nutty, my love for you\n" +
                        "I can't believe my dreams come true\n" +
                        "I've finally found somebody whose heart is true\n" +
                        "And best of all, you're nutty, nutty, nutty for me\n" +
                        "Nutty, nutty, nutty, my love for you\n" +
                        "I can't believe my dreams come true\n" +
                        "I've finally found somebody whose heart is true\n" +
                        "And best of all is you love me too\n" +
                        "Nutty, nutty, nutty, my love for you\n" +
                        "I can't believe my dreams come true\n" +
                        "I've finally found somebody whose heart is true\n" +
                        "And best of all is you're nutty, nutty, nutty for me\n" +
                        "\n" +
                        "Nutty, nutty, nutty, my love for you\n" +
                        "I can't believe my dreams come true\n" +
                        "I've finally found somebody whose heart is true\n" +
                        "And best of all is you love me too\n" +
                        "And nutty, nutty, nutty, my love for you\n" +
                        "I can't believe my dreams come true (I'm always doing that!)\n" +
                        "I've finally found somebody whose heart is true\n" +
                        "And best of all is you're nutty, nutty, nutty for me"
        ));
        this.songs.add(new Song(
                R.raw.walkaway,
                "Walk Away",
                "Kelly Clarkson",
                "You've got your mother and your brother\n" +
                        "Every other undercover telling you what to say (say)\n" +
                        "You think I'm stupid, but the truth is that it's cupid, baby\n" +
                        "Lovin' you has made me this way\n" +
                        "So before you point your finger\n" +
                        "Get your hands off of my trigger\n" +
                        "Oh, yeah\n" +
                        "You need to know this situation's getting old\n" +
                        "And now the more you talk, the less I can take, oh\n" +
                        "\n" +
                        "I'm looking for attention, not another question\n" +
                        "Should you stay or should you go?\n" +
                        "Well, if you don't have the answer\n" +
                        "Why are you still standin' here?\n" +
                        "Hey, hey, hey, hey\n" +
                        "Just walk away\n" +
                        "Just walk away\n" +
                        "Just walk away\n" +
                        "\n" +
                        "I waited here for you like a kid waiting after school\n" +
                        "So tell me how come you never showed? (never showed)\n" +
                        "I gave you everything and never asked for anything\n" +
                        "And look at me, I'm all alone (alone)\n" +
                        "So, before you start defending\n" +
                        "Baby, stop all your pretending\n" +
                        "I know you know I know\n" +
                        "So what's the point in being slow?\n" +
                        "Let's get the show on the road today\n" +
                        "Hey!\n" +
                        "\n" +
                        "I'm looking for attention, not another question\n" +
                        "Should you stay or should you go?\n" +
                        "Well, if you don't have the answer\n" +
                        "Why are you still standin' here?\n" +
                        "Hey, hey, hey, hey\n" +
                        "Just walk away\n" +
                        "Just walk away\n" +
                        "Just walk away\n" +
                        "\n" +
                        "I wanna love, I wanna fire\n" +
                        "To feel the burn, my desires\n" +
                        "I want a man by my side\n" +
                        "Not a boy who runs and hides\n" +
                        "Are you gonna fight for me?\n" +
                        "Die for me?\n" +
                        "Live and breathe for me?\n" +
                        "Do you care for me?\n" +
                        "'Cause if you don't, then boy, just leave\n" +
                        "\n" +
                        "I'm looking for attention, not another question\n" +
                        "Should you stay or should you go?\n" +
                        "Well, if you don't have the answer\n" +
                        "Why are you still standin' here?\n" +
                        "Hey, hey, hey, hey\n" +
                        "Just walk away\n" +
                        "Just walk away\n" +
                        "Just walk away\n" +
                        "\n" +
                        "If you don't have the answer\n" +
                        "Walk away\n" +
                        "Just walk (walk) away\n" +
                        "(Just walk away)\n" +
                        "Then just leave\n" +
                        "Yeah, yeah\n" +
                        "Walk away\n" +
                        "Walk away\n" +
                        "Walk away"
        ));
        this.songs.add(new Song(
                R.raw.enen,
                "Eh, Eh, Nothing Else I Can Say",
                "Lady Gaga",
                "CherryCherryBoomBoom\n" +
                        "Gaga\n" +
                        "\n" +
                        "Boy, we've had a real good time\n" +
                        "And I wish you the best on your way, eh-eh\n" +
                        "I didn't mean to hurt you\n" +
                        "I never thought we'd fall out of place, eh-eh, hey\n" +
                        "\n" +
                        "I have something that I love long, long\n" +
                        "But my friends keepa' tellin' me that something’s wrong\n" +
                        "Then I met someone\n" +
                        "And eh\n" +
                        "\n" +
                        "There's nothing else I can say\n" +
                        "Eh-eh, eh-eh\n" +
                        "There's nothing else I can say\n" +
                        "Eh-eh, eh-eh\n" +
                        "I wish he never looked at me that way\n" +
                        "Eh-eh, eh-eh\n" +
                        "There's nothing else I can say\n" +
                        "Eh-eh, hey-eh-ey\n" +
                        "\n" +
                        "Not that I don't care about you\n" +
                        "Just that things got so compliquées, eh-eh\n" +
                        "I met somebody cute and funny\n" +
                        "Got each other and that's funny, eh, eh-eh, hey\n" +
                        "\n" +
                        "I have something that I love long, long\n" +
                        "But my friends keepa' tellin' me that something’s wrong\n" +
                        "Then I met someone\n" +
                        "And eh\n" +
                        "\n" +
                        "There's nothing else I can say\n" +
                        "Eh-eh, eh-eh\n" +
                        "There's nothing else I can say\n" +
                        "Eh-eh, eh-eh\n" +
                        "I wish he never looked at me that way\n" +
                        "Eh-eh, eh-eh\n" +
                        "There's nothing else I can say\n" +
                        "Eh-eh, hey-eh-ey\n" +
                        "\n" +
                        "I have something that I love long, long\n" +
                        "But my friends keepa' tellin' me that something’s wrong\n" +
                        "Then I met someone\n" +
                        "And eh\n" +
                        "\n" +
                        "There's nothing else I can say\n" +
                        "Eh-eh, eh-eh\n" +
                        "Eh, eh-eh, eh-eh\n" +
                        "There's nothing else I can say\n" +
                        "Eh-eh, eh-eh\n" +
                        "I wish he never looked at me that way\n" +
                        "Eh-eh, hey-eh-ey (ooh, yeah)\n" +
                        "There's nothing else I can say (eh-eh)\n" +
                        "Eh-eh (eh-eh), eh-eh (eh-eh)\n" +
                        "I wish he never looked at me that way (eh-eh)\n" +
                        "Eh-eh (eh-eh), eh-eh (eh-eh)\n" +
                        "There's nothing else I can say (eh-eh)\n" +
                        "Eh-eh (eh-eh), eh-eh (eh-eh)\n" +
                        "\n" +
                        "CherryCherryBoomBoom\n" +
                        "Eh-eh, hey\n" +
                        "Oh yeah, all I can say is \"eh-eh\""
        ));
        this.songs.add(new Song(
                R.raw.entertainer,
                "The Entertainer",
                "Scott Joplin"
        ));
    }

    ArrayList<Song> getSongs() {
        return this.songs;
    }

    ArrayList<Integer> getSongsId() {
        ArrayList<Integer> songId = new ArrayList<>();
        for (Song song : this.songs) {
            songId.add(song.id);
        }
        return songId;
    }

}
