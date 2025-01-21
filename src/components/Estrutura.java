package components;

import java.time.LocalDate;
import java.util.*;


public class Estrutura {

    private static int RUN = 64;


    /* ========= ORDENAÇÃO NUMEROS INTEIROS ========= */
    public static void HeapSortNumber(List<Integer> list){
        int tam = list.size();

        for(int i = tam/2 - 1; i >= 0; i--){
            heapify(list, tam, i);
        }

        for (int i = tam - 1; i >= 0; i--) {

            int temp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temp);

            //exclui o ultimo elemento que já está ordenado.
            heapify(list, i, 0);
        }

    }

    public static void timSortNumber(List<Integer> list) {
        int n = list.size();

        // Ordena pequenos segmentos usando InsertionSort
        for (int i = 0; i < n; i += RUN) {
            insertionSortNumber(list, i, Math.min((i + RUN - 1), (n - 1)));
        }

        // Combina os segmentos usando Merge
        for (int size = RUN; size < n; size = 2 * size) {
            for (int left = 0; left < n; left += 2 * size) {
                int mid = left + size - 1;
                int right = Math.min((left + 2 * size - 1), (n - 1));

                if (mid < right) {
                    intMerge(list, left, mid, right);
                }
            }
        }
    }

    public static void MergeSortNumber(List<Integer> numberList){

        if(numberList.size() <= 1)
            return;
        
        int mid = numberList.size()/2; //meio da lista
        List<Integer> left = new ArrayList<>(numberList.subList(0, mid)); // Parte esquerda
        List<Integer> right = new ArrayList<>(numberList.subList(mid, numberList.size())); // Parte direita

        MergeSortNumber(left);
        MergeSortNumber(right);

        merge(numberList, left, right); //unir lista.
    }



    /* ========= ORDENAÇÃO PELA DATA ========= */
    public static void HeapSortData(List<String> list){
        int tam = list.size();

        for(int i = tam/2 - 1; i >= 0; i--){
            heapifyData(list, tam, i);
        }

        for (int i = tam - 1; i >= 0; i--) {

            String temp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temp);

            //exclui o ultimo elemento que já está ordenado.
            heapifyData(list, i, 0);
        }

    }

    public static void timSortData(List<String> list){
        int n = list.size();

        // Ordena pequenos segmentos usando InsertionSort
        for (int i = 0; i < n; i += RUN) {
            insertionSortData(list, i, Math.min((i + RUN - 1), (n - 1)));
        }

        // Combina os segmentos usando Merge
        for (int size = RUN; size < n; size = 2 * size) {
            for (int left = 0; left < n; left += 2 * size) {
                int mid = left + size - 1;
                int right = Math.min((left + 2 * size - 1), (n - 1));

                if (mid < right) {
                    dataMerge(list, left, mid, right);
                }
            }
        }

    }

    public static void MergeSortData(List<String> data){
        if(data.size() <= 1)
            return;
        
        int mid = data.size()/2; //meio da lista
        List<String> left = new ArrayList<>(data.subList(0, mid)); // Parte esquerda
        List<String> right = new ArrayList<>(data.subList(mid, data.size())); // Parte direita

        MergeSortData(left);
        MergeSortData(right);

        mergeByData(data, left, right); //unir lista.
    }


    /* ========= METODOS PRIVADOS PARA AS ESTRUTURAS ACIMA. ========= */

    private static void merge(List<Integer> result, List<Integer> left, List<Integer> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i) <= right.get(j)) {
                result.set(k, left.get(i));
                i++;
            } else {
                result.set(k, right.get(j));
                j++;
            }
            k++;
        }

        // Adiciona o restante dos elementos da lista esquerda (se houver)
        while (i < left.size()) {
            result.set(k, left.get(i));
            i++;
            k++;
        }

        // Adiciona o restante dos elementos da lista direita (se houver)
        while (j < right.size()) {
            result.set(k, right.get(j));
            j++;
            k++;
        }
    }

    private static void mergeByData(List<String> result, List<String> left, List<String> right){
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            LocalDate dataLeft = LocalDate.parse(left.get(i).split(":")[0]);
            LocalDate dataRight = LocalDate.parse(right.get(j).split(":")[0]);

            if (dataLeft.isBefore(dataRight) || dataLeft.equals(dataRight)) {
                result.set(k, left.get(i));
                i++;
            } else {
                result.set(k, right.get(j));
                j++;
            }
            k++;
        }

        while (i < left.size()) {
            result.set(k, left.get(i));
            i++;
            k++;
        }

        while (j < right.size()) {
            result.set(k, right.get(j));
            j++;
            k++;
        }

    }

    private static void heapify(List<Integer> data, int n, int i) {
        int largest = i; //maior nó
        int left = 2 * i + 1; //nó da esquerda
        int right = 2 * i + 2; //nó direita

        //se o nó da esquerda for maior que o nó atual.
        if (left < n && data.get(left) > data.get(largest)) {
            largest = left;
        }

        // se o nó da direita for maior que o nó atual.
        if (right < n && data.get(right) > data.get(largest)) {
            largest = right;
        }

        // se o maior nó não for o atual trocar.
        if (largest != i) {
            int swap = data.get(i);
            data.set(i, data.get(largest));
            data.set(largest, swap);

            heapify(data, n, largest);
        }
    }

    private static void heapifyData(List<String> data, int n, int i) {
        int largest = i; //maior nó
        int left = 2 * i + 1; //nó da esquerda
        int right = 2 * i + 2; //nó direita
    
        //se o nó da esquerda for maior que o nó atual.
        if (left < n && LocalDate.parse(data.get(left).split(":")[0]).isAfter(LocalDate.parse(data.get(largest).split(":")[0])) ){
            largest = left;
        }

        // se o nó da direita for maior que o nó atual.
        if (right < n && LocalDate.parse(data.get(right).split(":")[0]).isAfter(LocalDate.parse(data.get(largest).split(":")[0]))) {
            largest = right;
        }

        // se o maior nó não for o atual trocar.
        if (largest != i) {
            String swap = data.get(i);
            data.set(i, data.get(largest));
            data.set(largest, swap);

            heapifyData(data, n, largest);
        }
            
    }

    private static void insertionSortNumber(List<Integer> list, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int aux = list.get(i);
            int j = i - 1;

            while (j >= left && list.get(j) > aux) {
                list.set(j+1, list.get(j));
                j--;
            }
            list.set(j+1, aux);
        }
    }

    private static void intMerge(List<Integer> list, int left, int mid, int right){
        // Tamanhos dos dois subarrays
        int len1 = mid - left + 1, len2 = right - mid;

        // Cria arrays temporários para armazenar as sublistas
        List<Integer> leftList = new ArrayList<>(len1);
        List<Integer> rightList = new ArrayList<>(len2);

        // Copia dados para os arrays temporários
        for (int i = 0; i < len1; i++)
            leftList.add(list.get(left + i));
        for (int i = 0; i < len2; i++)
            rightList.add(list.get(mid + 1 + i));

        int i = 0, j = 0;
        int k = left;

        // Mescla os arrays temporários de volta no array original
        while (i < len1 && j < len2) {
            if (leftList.get(i) <= rightList.get(j)) {
                list.set(k, leftList.get(i));
                i++;
            } else {
                list.set(k, rightList.get(j));
                j++;
            }
            k++;
        }

        // Copia os elementos restantes de leftList
        while (i < len1) {
            list.set(k, leftList.get(i));
            i++;
            k++;
        }

        // Copia os elementos restantes de rightList
        while (j < len2) {
            list.set(k, rightList.get(j));
            j++;
            k++;
        }
    }

    private static void insertionSortData(List<String> list, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            String aux = list.get(i);
            int j = i - 1;

            // Comparação de datas entre elementos anteriores
            while (j >= left && LocalDate.parse(list.get(j).split(":")[0]).isAfter(LocalDate.parse(aux.split(":")[0]))) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, aux);
        }
    }

    private static void dataMerge(List<String> list, int left, int mid, int right){
        // Tamanhos dos dois subarrays
        int len1 = mid - left + 1, len2 = right - mid;

        // Cria arrays temporários para armazenar as sublistas
        List<String> leftList = new ArrayList<>(len1);
        List<String> rightList = new ArrayList<>(len2);

        // Copia dados para os arrays temporários
        for (int i = 0; i < len1; i++)
            leftList.add(list.get(left + i));
        for (int i = 0; i < len2; i++)
            rightList.add(list.get(mid + 1 + i));

        int i = 0, j = 0;
        int k = left;

        // Mescla os arrays temporários de volta no array original
        while (i < len1 && j < len2) {
            if (LocalDate.parse(leftList.get(i).split(":")[0]).isBefore(LocalDate.parse(rightList.get(j).split(":")[0]))) {
                list.set(k, leftList.get(i));
                i++;
            } else {
                list.set(k, rightList.get(j));
                j++;
            }
            k++;
        }

        // Copia os elementos restantes de leftList
        while (i < len1) {
            list.set(k, leftList.get(i));
            i++;
            k++;
        }

        // Copia os elementos restantes de rightList
        while (j < len2) {
            list.set(k, rightList.get(j));
            j++;
            k++;
        }
    }

}
