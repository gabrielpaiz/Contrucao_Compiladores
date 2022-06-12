/*
 * Integrantes:
 *  - Gabriel Bonatto Justo 17104118-9 gabriel.justo@acad.pucrs.br
 *  - Niccolas Guntzel Maganeli 16201269-4 niccolas.maganeli@edu.pucrs.br
 */

public class AppTeste {
    public static void main(String args[]) {
        INodo n1 = new NodoTDouble(3);
        INodo n2 = new NodoTDouble(4);
        INodo n3 = new NodoTDouble(5);

        INodo n4 = new NodoNT(TipoOperacao.MULL, n2, n3);
        INodo n5 = new NodoNT(TipoOperacao.ADD, n1, n4);

        INodo n6 = new NodoNT(TipoOperacao.LESS, n4, n5);
        INodo n7 = new NodoNT(TipoOperacao.LESS, n5, n4);

        System.out.println(n4 + " = " + n4.avalia());
        System.out.println(n5 + " = " + n5.avalia());
        System.out.println(n6 + " = " + n6.avalia());
        System.out.println(n7 + " = " + n7.avalia());

    }

}
