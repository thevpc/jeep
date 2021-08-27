package net.thevpc.jeep.editorkits;

public class Ansi256Colors {

    public static void main(String[] args) {
        for (int i = 0; i < 256; i++) {
            String si=String.valueOf(i);
            while(si.length()<3){
                si="0"+si;
            }
            System.out.println(
                "createColor("+i+",\"000000\"),"
            );
        }
    }
    public static final AnsiColor[] COLORS={
            createColor(0,"#000000"),
            createColor(1,"800000"),
            createColor(2,"#008000"),
            createColor(3,"#808000"),
            createColor(4,"#000080"),
            createColor(5,"#800080"),
            createColor(6,"#008080"),
            createColor(7,"#c0c0c0"),
            createColor(8,"#808080"),
            createColor(9,"#ff0000"),
            createColor(10,"#00ff00"),
            createColor(11,"#ffff00"),
            createColor(12,"#0000ff"),
            createColor(13,"#ff00ff"),
            createColor(14,"#00ffff"),
            createColor(15,"#ffffff"),
            createColor(16,"#000000"),
            createColor(17,"#00005f"),
            createColor(18,"#000087"),
            createColor(19,"#0000af"),
            createColor(20,"#0000d7"),
            createColor(21,"#0000ff"),
            createColor(22,"#005f00"),
            createColor(23,"#005f5f"),
            createColor(24,"#005f87"),
            createColor(25,"#005faf"),
            createColor(26,"#005fd7"),
            createColor(27,"#005fff"),
            createColor(28,"#008700"),
            createColor(29,"#00875f"),
            createColor(30,"#008787"),
            createColor(31,"#0087af"),
            createColor(32,"#0087d7"),
            createColor(33,"#0087ff"),
            createColor(34,"#00af00"),
            createColor(35,"#00af5f"),
            createColor(36,"#00af87"),
            createColor(37,"#00afaf"),
            createColor(38,"#00afd7"),
            createColor(39,"#00afff"),
            createColor(40,"#00d700"),
            createColor(41,"#00d75f"),
            createColor(42,"#00d787"),
            createColor(43,"#00d7af"),
            createColor(44,"#00d7d7"),
            createColor(45,"#00d7ff"),
            createColor(46,"#00ff00"),
            createColor(47,"#00ff5f"),
            createColor(48,"#00ff87"),
            createColor(49,"#00ffaf"),
            createColor(50,"#00ffd7"),
            createColor(51,"#00ffff"),
            createColor(52,"#5f0000"),
            createColor(53,"#5f005f"),
            createColor(54,"#5f0087"),
            createColor(55,"#5f00af"),
            createColor(56,"#5f00d7"),
            createColor(57,"#5f00ff"),
            createColor(58,"#5f5f00"),
            createColor(59,"#5f5f5f"),
            createColor(60,"#5f5f87"),
            createColor(61,"#5f5faf"),
            createColor(62,"#5f5fd7"),
            createColor(63,"#5f5fff"),
            createColor(64,"#5f8700"),
            createColor(65,"#5f875f"),
            createColor(66,"#5f8787"),
            createColor(67,"#5f87af"),
            createColor(68,"#5f87d7"),
            createColor(69,"#5f87ff"),
            createColor(70,"#5faf00"),
            createColor(71,"#5faf5f"),
            createColor(72,"#5faf87"),
            createColor(73,"#5fafaf"),
            createColor(74,"#5fafd7"),
            createColor(75,"#5fafff"),
            createColor(76,"#5fd700"),
            createColor(77,"#5fd75f"),
            createColor(78,"#5fd787"),
            createColor(79,"#5fd7af"),
            createColor(80,"#5fd7d7"),
            createColor(81,"#5fd7ff"),
            createColor(82,"#5fff00"),
            createColor(83,"#5fff5f"),
            createColor(84,"#5fff87"),
            createColor(85,"#5fffaf"),
            createColor(86,"#5fffd7"),
            createColor(87,"#5fffff"),
            createColor(88,"#870000"),
            createColor(89,"#87005f"),
            createColor(90,"#870087"),
            createColor(91,"#8700af"),
            createColor(92,"#8700d7"),
            createColor(93,"#8700ff"),
            createColor(94,"#875f00"),
            createColor(95,"#875f5f"),
            createColor(96,"#875f87"),
            createColor(97,"#875faf"),
            createColor(98,"#875fd7"),
            createColor(99,"#875fff"),
            createColor(100,"#878700"),
            createColor(101,"#87875f"),
            createColor(102,"#878787"),
            createColor(103,"#8787af"),
            createColor(104,"#8787d7"),
            createColor(105,"#8787ff"),
            createColor(106,"#87af00"),
            createColor(107,"#87af5f"),
            createColor(108,"#87af87"),
            createColor(109,"#87afaf"),
            createColor(110,"#87afd7"),
            createColor(111,"#87afff"),
            createColor(112,"#87d700"),
            createColor(113,"#87d75f"),
            createColor(114,"#87d787"),
            createColor(115,"#87d7af"),
            createColor(116,"#87d7d7"),
            createColor(117,"#87d7ff"),
            createColor(118,"#87ff00"),
            createColor(119,"#87ff5f"),
            createColor(120,"#87ff87"),
            createColor(121,"#87ffaf"),
            createColor(122,"#87ffd7"),
            createColor(123,"#87ffff"),
            createColor(124,"#af0000"),
            createColor(125,"#af005f"),
            createColor(126,"#af0087"),
            createColor(127,"#af00af"),
            createColor(128,"#af00d7"),
            createColor(129,"#af00ff"),
            createColor(130,"#af5f00"),
            createColor(131,"#af5f5f"),
            createColor(132,"#af5f87"),
            createColor(133,"#af5faf"),
            createColor(134,"#af5fd7"),
            createColor(135,"#af5fff"),
            createColor(136,"#af8700"),
            createColor(137,"#af875f"),
            createColor(138,"#af8787"),
            createColor(139,"#af87af"),
            createColor(140,"#af87d7"),
            createColor(141,"#af87ff"),
            createColor(142,"#afaf00"),
            createColor(143,"#afaf5f"),
            createColor(144,"#afaf87"),
            createColor(145,"#afafaf"),
            createColor(146,"#afafd7"),
            createColor(147,"#afafff"),
            createColor(148,"#afd700"),
            createColor(149,"#afd75f"),
            createColor(150,"#afd787"),
            createColor(151,"#afd7af"),
            createColor(152,"#afd7d7"),
            createColor(153,"#afd7ff"),
            createColor(154,"#afff00"),
            createColor(155,"#afff5f"),
            createColor(156,"#afff87"),
            createColor(157,"#afffaf"),
            createColor(158,"#afffd7"),
            createColor(159,"#afffff"),
            createColor(160,"#d70000"),
            createColor(161,"#d7005f"),
            createColor(162,"#d70087"),
            createColor(163,"#d700af"),
            createColor(164,"#d700d7"),
            createColor(165,"#d700ff"),
            createColor(166,"#d75f00"),
            createColor(167,"#d75f5f"),
            createColor(168,"#d75f87"),
            createColor(169,"#d75faf"),
            createColor(170,"#d75fd7"),
            createColor(171,"#d75fff"),
            createColor(172,"#d78700"),
            createColor(173,"#d7875f"),
            createColor(174,"#d78787"),
            createColor(175,"#d787af"),
            createColor(176,"#d787d7"),
            createColor(177,"#d787ff"),
            createColor(178,"#d7af00"),
            createColor(179,"#d7af5f"),
            createColor(180,"#d7af87"),
            createColor(181,"#d7afaf"),
            createColor(182,"#d7afd7"),
            createColor(183,"#d7afff"),
            createColor(184,"#d7d700"),
            createColor(185,"#d7d75f"),
            createColor(186,"#d7d787"),
            createColor(187,"#d7d7af"),
            createColor(188,"#d7d7d7"),
            createColor(189,"#d7d7ff"),
            createColor(190,"#d7ff00"),
            createColor(191,"#d7ff5f"),
            createColor(192,"#d7ff87"),
            createColor(193,"#d7ffaf"),
            createColor(194,"#d7ffd7"),
            createColor(195,"#d7ffff"),
            createColor(196,"#ff0000"),
            createColor(197,"#ff005f"),
            createColor(198,"#ff0087"),
            createColor(199,"#ff00af"),
            createColor(200,"#ff00d7"),
            createColor(201,"#ff00ff"),
            createColor(202,"#ff5f00"),
            createColor(203,"#ff5f5f"),
            createColor(204,"#ff5f87"),
            createColor(205,"#ff5faf"),
            createColor(206,"#ff5fd7"),
            createColor(207,"#ff5fff"),
            createColor(208,"#ff8700"),
            createColor(209,"#ff875f"),
            createColor(210,"#ff8787"),
            createColor(211,"#ff87af"),
            createColor(212,"#ff87d7"),
            createColor(213,"#ff87ff"),
            createColor(214,"#ffaf00"),
            createColor(215,"#ffaf5f"),
            createColor(216,"#ffaf87"),
            createColor(217,"#ffafaf"),
            createColor(218,"#ffafd7"),
            createColor(219,"#ffafff"),
            createColor(220,"#ffd700"),
            createColor(221,"#ffd75f"),
            createColor(222,"#ffd787"),
            createColor(223,"#ffd7af"),
            createColor(224,"#ffd7d7"),
            createColor(225,"#ffd7ff"),
            createColor(226,"#ffff00"),
            createColor(227,"#ffff5f"),
            createColor(228,"#ffff87"),
            createColor(229,"#ffffaf"),
            createColor(230,"#ffffd7"),
            createColor(231,"#ffffff"),
            createColor(232,"#080808"),
            createColor(233,"#121212"),
            createColor(234,"#1c1c1c"),
            createColor(235,"#262626"),
            createColor(236,"#303030"),
            createColor(237,"#3a3a3a"),
            createColor(238,"#444444"),
            createColor(239,"#4e4e4e"),
            createColor(240,"#585858"),
            createColor(241,"#626262"),
            createColor(242,"#6c6c6c"),
            createColor(243,"#767676"),
            createColor(244,"#808080"),
            createColor(245,"#8a8a8a"),
            createColor(246,"#949494"),
            createColor(247,"#9e9e9e"),
            createColor(248,"#a8a8a8"),
            createColor(249,"#b2b2b2"),
            createColor(250,"#bcbcbc"),
            createColor(251,"#c6c6c6"),
            createColor(252,"#d0d0d0"),
            createColor(253,"#dadada"),
            createColor(254,"#e4e4e4"),
            createColor(255,"#eeeeee")
    };

    private static AnsiColor createColor(int index, String n){
        if(n.startsWith("#")){
            n=n.substring(1);
        }
        int rgba = Integer.parseInt(n, 16);
        int red = ((rgba >> 16) & 0xFF);
        int green = ((rgba >> 8) & 0xFF);
        int blue = ((rgba >> 0) & 0xFF);
        return new AnsiColor(index,red,green,blue);
    }

    public static class AnsiColor {
        private final int index;
        private final int r;
        private final int g;
        private final int b;

        public AnsiColor(int rgba) {
            this.index=rgba;
            this.r = ((rgba >> 16) & 0xFF);
            this.g = ((rgba >> 8) & 0xFF);
            this.b = ((rgba >> 0) & 0xFF);
        }

        public AnsiColor(int index, int r, int g, int b) {
            this.index = index;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public int getIndex() {
            return index;
        }

        public int getR() {
            return r;
        }

        public int getG() {
            return g;
        }

        public int getB() {
            return b;
        }
    }
}
