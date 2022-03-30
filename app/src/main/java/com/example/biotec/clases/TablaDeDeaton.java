package com.example.biotec.clases;

import java.util.ArrayList;

public final class TablaDeDeaton {
    int[][] numIzq;
    int[][] deaton;

    public TablaDeDeaton(){
        llenarTablaIzq();
        llenarTablaDeaton();
    }


    public int numeroDeDeaton(int fila, int columna){
        llenarTablaDeaton();
        try {
            int result = deaton[fila][columna];
            return result;
        }catch (Exception e){
            return 0;
        }
    }

    public int[][] coordenadasDeaton(int numDeaton){
        int[][] coordenadas = new int[1][2];
        llenarTablaDeaton();

        for(int i =0;i <=30;i++) {
            for (int j = 0; j <= 11; j++) {
                if(deaton[i][j] == numDeaton){
                    coordenadas[0][0] = i;
                    coordenadas[0][1] = j;
                }
            }
        }

        return coordenadas;
    }

    public int[][] numIzq(int resultado){
        int[][] coordenadas = new int[1][2];
        llenarTablaIzq();

        if(resultado < 0){
            resultado *= -1;
            coordenadas = coordenadasDeaton(resultado);
        }else {

            for (int i = 0; i <= 30; i++) {
                for (int j = 0; j <= 11; j++) {
                    if (numIzq[i][j] == resultado) {
                        coordenadas[0][0] = i;
                        coordenadas[0][1] = j;
                    }
                }
            }
        }


        return coordenadas;
    }

    private void llenarTablaDeaton(){
        deaton = new int[31][11];
        int [][]deatonAux =
               // 01  02  03  04  05  06  07  08  09 10 11 12
               {{364,333,305,274,244,213,183,152,121,91,60,30},             //01
                {363,332,304,273,243,212,182,151,120,90,59,29},             //02
                {362,331,303,272,242,211,181,150,119,89,58,28},             //03
                {361,330,302,271,241,210,180,149,118,88,57,27},             //04
                {360,329,301,270,240,209,179,148,117,87,56,26},             //05
                {359,328,300,269,239,208,178,147,116,86,55,25},             //06
                {358,327,299,268,238,207,177,146,115,85,54,24},             //07
                {357,326,298,267,237,206,176,145,114,84,53,23},             //08
                {356,325,297,266,236,205,175,144,113,83,52,22},             //09
                {355,324,296,265,235,204,174,143,112,82,51,21},             //10
                {354,323,295,264,234,203,173,142,111,81,50,20},             //11
                {353,322,294,263,233,202,172,141,110,80,49,19},             //12
                {352,321,293,262,232,201,171,140,109,79,48,18},             //13
                {351,320,292,261,231,200,170,139,108,78,47,17},             //14
                {350,319,291,260,230,199,169,138,107,77,46,16},             //15
                {349,318,290,259,229,198,168,137,106,76,45,15},             //16
                {348,317,289,258,228,197,167,136,105,75,44,14},             //17
                {347,316,288,257,227,196,166,135,104,74,43,13},             //18
                {346,315,287,256,226,195,165,134,103,73,42,12},             //19
                {345,314,286,255,225,194,164,133,102,72,41,11},             //20
                {344,313,285,254,224,193,163,132,101,71,40,10},             //21
                {343,312,284,253,223,192,162,131,100,70,39,9},              //22
                {342,311,283,252,222,191,161,130,99,69,38,8},               //23
                {341,310,282,251,221,190,160,129,98,68,37,7},               //24
                {340,309,281,250,220,189,159,128,97,67,36,6},               //25
                {339,308,280,249,219,188,158,127,96,66,35,5},               //26
                {338,307,279,248,218,187,157,126,95,65,34,4},               //27
                {337,306,278,247,217,186,156,125,94,64,33,3},               //28
                {336,0,277,246,216,185,155,124,93,63,32,2},                 //29
                {335,0,276,245,215,184,154,123,92,62,31,1},                 //30
                {334,0,275,0,214,0,153,122,0,61,0,0 }                       //31

        };

        deaton = deatonAux;

    }

    private void llenarTablaIzq() {
        numIzq = new int[31][12];
        int num = 1;

        for(int i = 0; i <=11;i++){
            for(int j=0;j <=30;j++){
                numIzq[j][i] = num;
                if(i==1 && (j==28 || j==29 || j==30)){
                    numIzq[j][i] = 0;
                    continue;
                }else if(i==3 && j==30){
                    numIzq[j][i] = 0;
                    continue;
                }else if(i==5 && j == 30){
                    numIzq[j][i] = 0;
                    continue;
                }else if(i == 8 && j==30){
                    numIzq[j][i] = 0;
                    continue;
                }else if (i ==10 &&j== 30){
                    numIzq[j][i] = 0;
                    continue;
                }
                num++;
            }
        }
    }

    public int[][] getNumIzq() {
        return numIzq;
    }

    public void setNumIzq(int[][] numIzq) {
        this.numIzq = numIzq;
    }

    public int[][] getDeaton() {
        return deaton;
    }

    public void setDeaton(int[][] deaton) {
        this.deaton = deaton;
    }
}
