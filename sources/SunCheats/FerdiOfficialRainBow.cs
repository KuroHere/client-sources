﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Rainbow
{
    public static class Class2
    {
        public static int A = 148;
        public static int R = 0;
        public static int G = 211;
        public static int runlock = 1;

        public static void RainbowEffect()
        {

            // RUNLOCK 1

            if (runlock == 1)
            {
                if (A == 75)
                {
                    // Nothing
                }
                else
                {
                    A -= 1;
                }

                if (G == 130)
                {
                    if (A == 75)
                    {
                        runlock += 1;
                    }
                    else
                    {
                        A -= 1;
                    }
                }
                else
                {
                    G -= 1;
                }
            }

            

            if (runlock == 2)
            {

                if (G == 254)
                {
                    if (A == 0)
                    {
                        runlock += 1;
                    }
                    else
                    {
                        A -= 1;
                    }
                }
                else
                {
                    G += 1;
                }
            }

          

            if (runlock == 3)
            {
                if (R == 254)
                {
                    if (G == 1)
                    {
                        runlock += 1;
                    }
                    else
                    {
                        G -= 1;
                    }
                }
                else
                {
                    R += 1;
                }
            }

           

            if (runlock == 4)
            {
                if (A == 254)
                {
                    if (R == 254)
                    {
                        runlock += 1;
                    }
                    else
                    {
                        R = 254;
                    }
                }
                else
                {
                    A += 1;
                }
            }

            

            if (runlock == 5)
            {
                if (A == 254)
                {
                    if (R == 127)
                    {
                        runlock += 1;
                    }
                    else
                    {
                        R -= 1;
                    }
                }
                else
                {
                    A = 254;
                }
            }

            

            if (runlock == 6)
            {
                if (R == 1)
                {
                    if (A == 254)
                    {
                        //A = 148; // RED
                        //R = 0;   // GREEN
                        //G = 211;   // BLUE
                        runlock += 1;
                    }
                    else
                    {
                        A = 254;
                    }
                }
                else
                {
                    R -= 1;
                }
            }


           
            if (runlock == 7)
            {
                if (A == 148)
                {
                    if (G == 211)
                    {
                        A = 148; 
                        R = 0;   
                        G = 211;   
                        runlock = 1;
                    }
                    else
                    {
                        G += 1;
                    }
                }
                else
                {
                    A -= 1;
                }
            }

            

        }
    }
}