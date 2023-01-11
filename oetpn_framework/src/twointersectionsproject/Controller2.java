package twointersectionsproject;

import com.Components.Activation;
import com.Components.Condition;
import com.Components.GuardMapping;
import com.Components.PetriNet;
import com.Components.PetriNetWindow;
import com.Components.PetriTransition;
import com.DataObjects.DataInteger;
import com.DataObjects.DataString;
import com.DataObjects.DataTransfer;
import com.DataOnly.TransferOperation;
import com.Enumerations.LogicConnector;
import com.Enumerations.TransitionCondition;
import com.Enumerations.TransitionOperation;

public class Controller2 {

    public static void main(String[] args) {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Controller";
        pn.SetName("Controller");
        pn.NetworkPort = 1084;

        DataString ini = new DataString();
        //ini.Printable = false;
        ini.SetName("ini");
        ini.SetValue("red");
        pn.ConstantPlaceList.add(ini);

        DataString red = new DataString();
        //red.Printable = false;
        red.SetName("red");
        red.SetValue("red");
        pn.ConstantPlaceList.add(red);

        DataString green = new DataString();
        //green.Printable = false;
        green.SetName("green");
        green.SetValue("green");
        pn.ConstantPlaceList.add(green);

        DataString yellow = new DataString();
        //yellow.Printable = false;
        yellow.SetName("yellow");
        yellow.SetValue("yellow");
        pn.ConstantPlaceList.add(yellow);

        DataString p1 = new DataString();
        p1.SetName("r1r2");
        p1.SetValue("signal");
        pn.PlaceList.add(p1);

        DataString p2 = new DataString();
        p2.SetName("g1r2");
        pn.PlaceList.add(p2);

        DataString p3 = new DataString();
        p3.SetName("y1r2");
        pn.PlaceList.add(p3);

        DataString p4 = new DataString();
        p4.SetName("r1g2");
        pn.PlaceList.add(p4);

        DataString p5 = new DataString();
        p5.SetName("r1y2");
        pn.PlaceList.add(p5);

        DataTransfer p8 = new DataTransfer();
        p8.SetName("OP3");
        p8.Value = new TransferOperation("localhost", "1082", "P_TL3");
        pn.PlaceList.add(p8);

        DataTransfer p9 = new DataTransfer();
        p9.SetName("OP4");
        p9.Value = new TransferOperation("localhost", "1082", "P_TL4");
        pn.PlaceList.add(p9);

        DataString in5 = new DataString();
        in5.SetName("in5");
        pn.PlaceList.add(in5);

        DataString in6 = new DataString();
        in6.SetName("in6");
        pn.PlaceList.add(in6);

        DataInteger fifteen = new DataInteger();
        fifteen.SetName("Fifteen");
        fifteen.SetValue(15);
        pn.ConstantPlaceList.add(fifteen);

        DataInteger One = new DataInteger();
        One.SetName("One");
        One.SetValue(1);
        pn.ConstantPlaceList.add(One);


        //----------------------------iniT------------------------------------
        PetriTransition iniT = new PetriTransition(pn);
        iniT.TransitionName = "iniT";

        Condition iniTCt1 = new Condition(iniT, "ini", TransitionCondition.NotNull);

        GuardMapping grdiniT = new GuardMapping();
        grdiniT.condition = iniTCt1;

        grdiniT.Activations.add(new Activation(iniT, "ini", TransitionOperation.SendOverNetwork, "OP3"));
        grdiniT.Activations.add(new Activation(iniT, "ini", TransitionOperation.SendOverNetwork, "OP4"));
        grdiniT.Activations.add(new Activation(iniT, "", TransitionOperation.MakeNull, "ini"));

        iniT.GuardMappingList.add(grdiniT);

        iniT.Delay = 0;
        pn.Transitions.add(iniT);


        //----------------------------T1------------------------------------
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T1";
        t1.InputPlaceName.add("r1r2");


        Condition T1Ct1 = new Condition(t1, "r1r2", TransitionCondition.NotNull);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;
        grdT1.Activations.add(new Activation(t1, "r1r2", TransitionOperation.Move, "g1r2"));
        grdT1.Activations.add(new Activation(t1, "green", TransitionOperation.SendOverNetwork, "OP3"));
        t1.GuardMappingList.add(grdT1);

        t1.Delay = 5;
        pn.Transitions.add(t1);

        //----------------------------T2------------------------------------
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "T2";
        t2.InputPlaceName.add("g1r2");
        t2.InputPlaceName.add("in5");

        Condition T2Ct1 = new Condition(t2, "g1r2", TransitionCondition.NotNull);
        Condition T2Ct2 = new Condition(t2,"in5",TransitionCondition.NotNull);

        T2Ct1.SetNextCondition(LogicConnector.AND, T2Ct2);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;
        grdT2.Activations.add(new Activation(t2, "g1r2", TransitionOperation.Move, "y1r2"));
        grdT2.Activations.add(new Activation(t2, "yellow", TransitionOperation.SendOverNetwork, "OP3"));
        grdT2.Activations.add(new Activation(t2, "Fifteen", TransitionOperation.DynamicDelay,""));

        t2.GuardMappingList.add(grdT2);


        //---------------------------- guard when the queue is not full------------------------------------
        Condition T2Ct3 = new Condition(t2, "g1r2", TransitionCondition.NotNull);
        Condition T2Ct4 = new Condition(t2,"in5",TransitionCondition.IsNull);

        T2Ct3.SetNextCondition(LogicConnector.AND, T2Ct4);

        GuardMapping grdT2_2 = new GuardMapping();
        grdT2_2.condition = T2Ct3;
        grdT2_2.Activations.add(new Activation(t2, "g1r2", TransitionOperation.Move, "y1r2"));
        grdT2_2.Activations.add(new Activation(t2, "yellow", TransitionOperation.SendOverNetwork, "OP3"));
        grdT2_2.Activations.add(new Activation(t2, "One", TransitionOperation.DynamicDelay,""));

        t2.GuardMappingList.add(grdT2_2);

        t2.Delay = 5;
        pn.Transitions.add(t2);


        //----------------------------T3------------------------------------
        PetriTransition t3 = new PetriTransition(pn);
        t3.TransitionName = "T3";
        t3.InputPlaceName.add("y1r2");


        Condition T3Ct1 = new Condition(t2, "y1r2", TransitionCondition.NotNull);

        GuardMapping grdT3 = new GuardMapping();
        grdT3.condition = T3Ct1;
        grdT3.Activations.add(new Activation(t3, "y1r2", TransitionOperation.Move, "r1g2"));
        grdT3.Activations.add(new Activation(t3, "red", TransitionOperation.SendOverNetwork, "OP3"));
        grdT3.Activations.add(new Activation(t3, "green", TransitionOperation.SendOverNetwork, "OP4"));

        t3.GuardMappingList.add(grdT3);

        t3.Delay = 5;
        pn.Transitions.add(t3);


        //----------------------------T4------------------------------------
        PetriTransition t4 = new PetriTransition(pn);
        t4.TransitionName = "T4";
        t4.InputPlaceName.add("r1g2");
        t4.InputPlaceName.add("in6");

        Condition T4Ct1 = new Condition(t4, "r1g2", TransitionCondition.NotNull);
        Condition T4Ct2 = new Condition(t4,"in6",TransitionCondition.NotNull);

        T4Ct1.SetNextCondition(LogicConnector.AND, T4Ct2);

        GuardMapping grdT4 = new GuardMapping();
        grdT4.condition = T4Ct1;
        grdT4.Activations.add(new Activation(t4, "r1g2", TransitionOperation.Move, "r1y2"));
        grdT4.Activations.add(new Activation(t4, "yellow", TransitionOperation.SendOverNetwork, "OP4"));
        grdT4.Activations.add(new Activation(t4, "Fifteen", TransitionOperation.DynamicDelay,""));

        t4.GuardMappingList.add(grdT4);


        //---------------------------- guard when the queue is not full------------------------------------
        Condition T4Ct3 = new Condition(t4, "r1g2", TransitionCondition.NotNull);
        Condition T4Ct4 = new Condition(t4,"in6", TransitionCondition.IsNull);

        T4Ct3.SetNextCondition(LogicConnector.AND, T4Ct4);

        GuardMapping grdT4_2 = new GuardMapping();
        grdT4_2.condition = T4Ct3;
        grdT4_2.Activations.add(new Activation(t4, "r1g2", TransitionOperation.Move, "r1y2"));
        grdT4_2.Activations.add(new Activation(t4, "yellow", TransitionOperation.SendOverNetwork, "OP4"));
        grdT4_2.Activations.add(new Activation(t4, "One", TransitionOperation.DynamicDelay,""));

        t4.GuardMappingList.add(grdT4_2);

        t4.Delay = 5;
        pn.Transitions.add(t4);


        //----------------------------T5------------------------------------
        PetriTransition t5 = new PetriTransition(pn);
        t5.TransitionName = "T5";
        t5.InputPlaceName.add("r1y2");


        Condition T5Ct1 = new Condition(t2, "r1y2", TransitionCondition.NotNull);

        GuardMapping grdT5 = new GuardMapping();
        grdT5.condition = T5Ct1;
        grdT5.Activations.add(new Activation(t5, "r1y2", TransitionOperation.Move, "r1r2"));
        grdT5.Activations.add(new Activation(t5, "red", TransitionOperation.SendOverNetwork, "OP4"));


        t5.GuardMappingList.add(grdT5);

        t5.Delay = 5;
        pn.Transitions.add(t5);

        // -------------------------------------------------------------------------------------
        // ----------------------------PN Start-------------------------------------------------
        // -------------------------------------------------------------------------------------

        System.out.println("Exp1 started \n ------------------------------");
        pn.Delay = 2000;
        // pn.Start();

        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);


    }
}
