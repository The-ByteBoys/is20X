package tools;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class PointCalculatorTest {

    @Test
    void getMean() {
        ArrayList<Double> numbers1 = new ArrayList<>();
        numbers1.add(360.0);
        numbers1.add(320.7);
        numbers1.add(297.2);
        numbers1.add(275.0);
        numbers1.add(288.0);
        numbers1.add(275.0);
        numbers1.add(262.5);
        numbers1.add(242.0);
        numbers1.add(241.5);
        numbers1.add(236.4);
        numbers1.add(237.0);
        numbers1.add(224.0);
        numbers1.add(224.0);
        numbers1.add(237.0);
        numbers1.add(215.1);
        numbers1.add(223.0);
        numbers1.add(213.5);
        numbers1.add(197.0);
        numbers1.add(221.0);
        numbers1.add(179.4);
        numbers1.add(260.0);
        numbers1.add(187.7);
        numbers1.add(218.0);
        numbers1.add(233.0);
        numbers1.add(210.0);
        numbers1.add(180.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);

        PointCalculator pointCalc = new PointCalculator(numbers1);
        double result = pointCalc.getMean();
        assert  result > 145.0 && result < 145.2;
    }

    @Test
    void getStdev() {
        ArrayList<Double> numbers1 = new ArrayList<>();
        numbers1.add(470.0);
        numbers1.add(390.0);
        numbers1.add(342.8);
        numbers1.add(340.0);
        numbers1.add(329.2);
        numbers1.add(332.0);
        numbers1.add(309.2);
        numbers1.add(285.0);
        numbers1.add(274.5);
        numbers1.add(277.6);
        numbers1.add(285.0);
        numbers1.add(264.0);
        numbers1.add(271.0);
        numbers1.add(284.0);
        numbers1.add(268.0);
        numbers1.add(243.0);
        numbers1.add(249.9);
        numbers1.add(246.3);
        numbers1.add(255.9);
        numbers1.add(233.3);
        numbers1.add(-1.0);
        numbers1.add(219.0);
        numbers1.add(237.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(213.0);
        numbers1.add(226.0);
        numbers1.add(306.2);
        numbers1.add(310.2);
        numbers1.add(249.0);
        numbers1.add(213.3);
        numbers1.add(211.5);
        numbers1.add(289.8);
        numbers1.add(270.2);
        numbers1.add(205.5);
        numbers1.add(174.5);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);

        PointCalculator pointCalc = new PointCalculator(numbers1);

        double result = pointCalc.getStdev();
        System.out.print(result);

        assert result > 120 && result < 130.0;
    }

    @Test
    void getPoints() {
        ArrayList<Double> numbers1 = new ArrayList<>();
        /*numbers1.add(360.0);
        numbers1.add(320.7);
        numbers1.add(297.2);
        numbers1.add(275.0);
        numbers1.add(288.0);
        numbers1.add(275.0);
        numbers1.add(262.5);
        numbers1.add(242.0);
        numbers1.add(241.5);
        numbers1.add(236.4);
        numbers1.add(237.0);
        numbers1.add(224.0);
        numbers1.add(224.0);
        numbers1.add(237.0);
        numbers1.add(215.1);
        numbers1.add(223.0);
        numbers1.add(213.5);
        numbers1.add(197.0);
        numbers1.add(221.0);
        numbers1.add(179.4);
        numbers1.add(260.0);
        numbers1.add(187.7);
        numbers1.add(218.0);
        numbers1.add(233.0);
        numbers1.add(210.0);
        numbers1.add(180.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
        numbers1.add(-1.0);
         */

        numbers1.add(391.0);
        numbers1.add(355.0);
        numbers1.add(336.0);
        numbers1.add(332.0);
        numbers1.add(374.0);
        numbers1.add(317.0);
        numbers1.add(317.0);
        numbers1.add(317.0);
        numbers1.add(323.0);
        numbers1.add(361.0);
        numbers1.add(343.0);
        numbers1.add(345.0);
        numbers1.add(278.0);
        numbers1.add(261.0);
        numbers1.add(295.0);
        numbers1.add(266.0);
        numbers1.add(286.0);
        numbers1.add(310.0);
        numbers1.add(253.0);
        numbers1.add(295.0);
        numbers1.add(297.0);
        numbers1.add(262.0);
        numbers1.add(292.0);
        numbers1.add(257.0);
        numbers1.add(249.0);
        numbers1.add(227.0);
        numbers1.add(242.0);
        numbers1.add(248.0);
        numbers1.add(252.0);
        numbers1.add(207.0);
        numbers1.add(243.0);
        numbers1.add(184.0);
        numbers1.add(285.0);
        numbers1.add(297.0);
        numbers1.add(344.0);
        numbers1.add(339.0);
        numbers1.add(196.0);
        numbers1.add(286.0);
        numbers1.add(231.0);
        numbers1.add(0.0);
        numbers1.add(0.0);
        numbers1.add(199.0);
        numbers1.add(0.0);
        numbers1.add(0.0);
        numbers1.add(0.0);
        numbers1.add(0.0);
        numbers1.add(0.0);
        numbers1.add(157.0);
        numbers1.add(0.0);
        numbers1.add(150.0);
        numbers1.add(0.0);

        ArrayList<Double> numbers2 = new ArrayList<>();
        numbers2.add(470.0);
        numbers2.add(390.0);
        numbers2.add(342.8);
        numbers2.add(340.0);
        numbers2.add(329.2);
        numbers2.add(332.0);
        numbers2.add(309.2);
        numbers2.add(285.0);
        numbers2.add(274.5);
        numbers2.add(277.6);
        numbers2.add(285.0);
        numbers2.add(264.0);
        numbers2.add(271.0);
        numbers2.add(284.0);
        numbers2.add(268.0);
        numbers2.add(243.0);
        numbers2.add(249.9);
        numbers2.add(246.3);
        numbers2.add(255.9);
        numbers2.add(233.3);
        numbers2.add(-1.0);
        numbers2.add(219.0);
        numbers2.add(237.0);
        numbers2.add(-1.0);
        numbers2.add(-1.0);
        numbers2.add(213.0);
        numbers2.add(226.0);
        numbers2.add(306.2);
        numbers2.add(310.2);
        numbers2.add(249.0);
        numbers2.add(213.3);
        numbers2.add(211.5);
        numbers2.add(289.8);
        numbers2.add(270.2);
        numbers2.add(205.5);
        numbers2.add(174.5);
        numbers2.add(-1.0);
        numbers2.add(-1.0);
        numbers2.add(-1.0);
        numbers2.add(-1.0);
        numbers2.add(-1.0);
        numbers2.add(-1.0);
        numbers2.add(-1.0);

        ArrayList<Double> numbers3 = new ArrayList<>();
        numbers3.add(713.2);
        numbers3.add(622.2);
        numbers3.add(564.3);
        numbers3.add(562.0);
        numbers3.add(507.5);
        numbers3.add(533.2);
        numbers3.add(509.4);
        numbers3.add(535.3);
        numbers3.add(464.9);
        numbers3.add(494.1);
        numbers3.add(454.0);
        numbers3.add(484.0);
        numbers3.add(436.4);
        numbers3.add(504.0);
        numbers3.add(486.3);
        numbers3.add(454.0);
        numbers3.add(385.3);
        numbers3.add(513.1);
        numbers3.add(-1.0);
        numbers3.add(338.6);
        numbers3.add(504.4);
        numbers3.add(326.1);
        numbers3.add(-1.0);
        numbers3.add(426.9);
        numbers3.add(471.0);
        numbers3.add(-1.0);
        numbers3.add(385.7);
        numbers3.add(-1.0);
        numbers3.add(-1.0);
        numbers3.add(430.3);
        numbers3.add(343.7);
        numbers3.add(365.7);
        numbers3.add(-1.0);
        numbers3.add(-1.0);
        numbers3.add(341.2);
        numbers3.add(277.3);
        numbers3.add(583.0);
        numbers3.add(506.0);
        numbers3.add(494.6);
        numbers3.add(594.6);
        numbers3.add(399.0);
        numbers3.add(313.8);
        numbers3.add(-1.0);

        ArrayList<Double> numbers4 = new ArrayList<>();
        numbers4.add(84.0);
        numbers4.add(85.0);
        numbers4.add(88.0);
        numbers4.add(92.0);
        numbers4.add(89.0);
        numbers4.add(92.0);
        numbers4.add(92.0);
        numbers4.add(118.0);
        numbers4.add(101.0);
        numbers4.add(95.0);
        numbers4.add(89.0);
        numbers4.add(97.0);
        numbers4.add(89.0);
        numbers4.add(100.0);
        numbers4.add(77.0);
        numbers4.add(97.0);
        numbers4.add(83.0);
        numbers4.add(68.0);
        numbers4.add(-1.0);
        numbers4.add(-1.0);
        numbers4.add(88.0);
        numbers4.add(-1.0);
        numbers4.add(-1.0);
        numbers4.add(77.0);
        numbers4.add(89.0);
        numbers4.add(-1.0);
        numbers4.add(91.0);
        numbers4.add(113.0);
        numbers4.add(97.0);
        numbers4.add(-1.0);
        numbers4.add(96.0);
        numbers4.add(89.0);
        numbers4.add(70.0);
        numbers4.add(100.0);
        numbers4.add(-1.0);
        numbers4.add(83.0);
        numbers4.add(110.0);
        numbers4.add(96.0);
        numbers4.add(93.0);
        numbers4.add(88.0);
        numbers4.add(-1.0);
        numbers4.add(-1.0);
        numbers4.add(-1.0);

        ArrayList<Double> numbers5 = new ArrayList<>();
        numbers5.add(48.0);
        numbers5.add(36.0);
        numbers5.add(41.0);
        numbers5.add(48.0);
        numbers5.add(43.0);
        numbers5.add(47.0);
        numbers5.add(40.0);
        numbers5.add(48.0);
        numbers5.add(51.0);
        numbers5.add(52.0);
        numbers5.add(50.0);
        numbers5.add(57.0);
        numbers5.add(60.0);
        numbers5.add(-1.0);
        numbers5.add(43.0);
        numbers5.add(50.0);
        numbers5.add(36.5);
        numbers5.add(38.0);
        numbers5.add(42.0);
        numbers5.add(55.0);
        numbers5.add(43.0);
        numbers5.add(-1.0);
        numbers5.add(-1.0);
        numbers5.add(43.0);
        numbers5.add(43.0);
        numbers5.add(-1.0);
        numbers5.add(40.0);
        numbers5.add(-1.0);
        numbers5.add(-1.0);
        numbers5.add(-1.0);
        numbers5.add(-1.0);
        numbers5.add(-1.0);
        numbers5.add(-1.0);
        numbers5.add(-1.0);
        numbers5.add(40.0);
        numbers5.add(-1.0);
        numbers5.add(38.0);
        numbers5.add(44.0);
        numbers5.add(44.0);
        numbers5.add(-1.0);
        numbers5.add(-1.0);
        numbers5.add(-1.0);
        numbers5.add(52.0);

        ArrayList<Double> numbers6 = new ArrayList<>();
        numbers6.add(2.0);
        numbers6.add(3.0);
        numbers6.add(2.0);
        numbers6.add(2.0);
        numbers6.add(3.0);
        numbers6.add(2.0);
        numbers6.add(3.0);
        numbers6.add(3.0);
        numbers6.add(3.0);
        numbers6.add(1.0);
        numbers6.add(2.0);
        numbers6.add(1.0);
        numbers6.add(2.0);
        numbers6.add(2.0);
        numbers6.add(1.0);
        numbers6.add(-1.0);
        numbers6.add(1.0);
        numbers6.add(1.0);
        numbers6.add(3.0);
        numbers6.add(2.0);
        numbers6.add(1.0);
        numbers6.add(2.0);
        numbers6.add(-1.0);
        numbers6.add(1.0);
        numbers6.add(1.0);
        numbers6.add(-1.0);
        numbers6.add(2.0);
        numbers6.add(1.0);
        numbers6.add(-1.0);
        numbers6.add(2.0);
        numbers6.add(-1.0);
        numbers6.add(-1.0);
        numbers6.add(-1.0);
        numbers6.add(-1.0);
        numbers6.add(2.0);
        numbers6.add(-1.0);
        numbers6.add(3.0);
        numbers6.add(3.0);
        numbers6.add(1.0);
        numbers6.add(3.0);
        numbers6.add(-1.0);
        numbers6.add(2.0);
        numbers6.add(1.0);

        PointCalculator calc1 = new PointCalculator(numbers1);
        PointCalculator calc2 = new PointCalculator(numbers2);
        PointCalculator calc3 = new PointCalculator(numbers3);
        PointCalculator calc4 = new PointCalculator(numbers4);
        PointCalculator calc5 = new PointCalculator(numbers5);
        PointCalculator calc6 = new PointCalculator(numbers6);

        double points1 = calc1.getPoints(391, 45);
        double points2 = calc2.getPoints(470, 30);
        double points3 = calc3.getPoints(713.2, 10);
        double points4 = calc4.getPoints(84, 5);
        double points5 = calc5.getPoints(48, 5);
        double points6 = calc6.getPoints(2, 5);

        System.out.println(points1);
        System.out.println(points2);
        System.out.println(points3);
        System.out.println(points4);
        System.out.println(points5);
        System.out.println(points6);

        double totalPoints = points1+points2+points3+points4+points5+points6;
        System.out.println("Total points (1): "+totalPoints);
        double result = totalPoints*10+80;
        System.out.println(result);

        double totalPoints2 = calc1.getPoints(320.7, 45)+
                calc2.getPoints(390, 30)+
                calc3.getPoints(713.2, 10)+
                calc4.getPoints(84, 5)+
                calc5.getPoints(48, 5)+
                calc6.getPoints(2, 5);
        System.out.println("Total points (2): "+totalPoints2);
        System.out.println(totalPoints2*10+80);

        double totalPoints3 = calc1.getPoints(260, 45)+
                calc2.getPoints(-1, 30)+
                calc3.getPoints(504.4, 10)+
                calc4.getPoints(88, 5)+
                calc5.getPoints(43, 5)+
                calc6.getPoints(1, 5);
        System.out.println("Total points (3): "+totalPoints3);
        System.out.println(totalPoints3*10+80);

        System.out.println("Score 2000: "+calc2.getPoints(-1, 30));
        System.out.println("Score 2000: "+calc2.getPoints(0, 30));

        System.out.println("Test without weight:");
        System.out.println(calc1.getPoints(200, 0));
        System.out.println("Test with weight:");
        System.out.println(calc1.getPoints(200, 10));



//        assert result == 0.6;
    }
}