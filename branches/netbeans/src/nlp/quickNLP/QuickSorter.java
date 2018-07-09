/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.quickNLP;

import java.util.Vector;

/**
 *
 * @author Chris
 */
public class QuickSorter {

    /** Partition sparator, found the index position of latest element. And
     * return the location of the separator.
     *
     * @param   nBegin
     *          start point of the partation/interval;
     * @param   nEnd
     *          start point of the partation/interval;
     * @param   pData
     *          data which need to be resort.
     */
    //化分区间,找到最后元素的排序位置。并返回分隔的点（即最后一数据排序的位置）。
    //划分的区间是[nBegin, nEnd). pData是保存数据的指针
    private int Partition(Vector<Concept> pData, int nBegin, int nEnd)
    {
        double part = Math.random();
        int i = (int)(nBegin +  0.5 %(nEnd - nBegin));
        System.out.println(i + "; nBegin =" + nBegin + " , nEnd = " + nEnd);
        //这里是和hoare的思路写的，和原版本不是完全一样，思路是一样的。
        Concept x = pData.get(i);
        //pData[i] = pData[nBegin];
        pData.setElementAt( pData.get(nBegin), i);
        //pData[nBegin] = x;
        pData.setElementAt(x, nBegin);
        //int x = pData[nBegin];
        --nEnd;

        while (nBegin < nEnd)
        {
            //从后向前，找到比X小的元素位置
            //while(pData[nEnd] > x)
            while( compare( pData.get(nEnd) , x ) )
            {
                --nEnd;
            }
            //把x小的元素位置提前，nBegin处刚好能保存比x小的元素
            if (nBegin < nEnd)
            {
                //pData[nBegin] = pData[nEnd];
                pData.setElementAt( pData.get(nEnd), nBegin);
                //pData[nEnd] = x;    //这里是为了做一个哨兵，防止小区域增加时越界。
                pData.setElementAt(x, nEnd);
                ++nBegin;
            }

            //小的区域增加，找到一个不比x小的元素。
            //while ( pData[nBegin] < x)
            while ( !compare( pData.get(nBegin) , x) )
            {
                ++nBegin;
            }

            //把不比x小的元素存放在大的区域内。nEnd刚好预留了此位置。
            if (nBegin < nEnd)
            {
                //pData[nEnd] = pData[nBegin];
                pData.setElementAt( pData.get(nBegin), nEnd);
                --nEnd;
            }
        }

        //pData[nBegin] = x;    //这里一定要赋值，不然如果是nEnd退出循环，他是保存着以前的大值，会出错。
        pData.setElementAt(x, nBegin);
        return nBegin;   //返回nD的位置，就是分割的位置。
    }

    //排序的递归调用。
    private int QuickSortRecursion(Vector<Concept> pData, int nBeging, int nEnd)
    {
        if (nBeging >= nEnd -1)        //如果区域不存在或只有一个数据则不递归排序
        {
            return 1;
        }

        //这里因为分割的时候，分割点处的数据就是排序中他的位置。
        //也就是说他的左边的数据都小于等于他，他右边的数据都大于他。
        //所以他不在递归调用的数据中。
        int pivto = Partition(pData, nBeging, nEnd);        //找到分割点

        QuickSortRecursion(pData, nBeging, pivto);            //递归左边的排序
        QuickSortRecursion(pData, pivto + 1, nEnd);            //递归右边的排序

        return 1;
    }

    //快速排序
    public void QuickSort(Vector<Concept> concepts)
    {
        int nLen = concepts.size();
        //递归调用，快速排序。
        QuickSortRecursion(concepts, 0, nLen);
    }


    private boolean compare(Concept o1,Concept o2) {
        try{
        String ac1_classname = o1.term.trim();
        String ac2_classname = o2.term.trim();

        int size1 = ac1_classname.length();
        int size2 = ac2_classname.length();

        int size = ( size1 < size2 ? size1 : size2);

        char[] chars1 = ac1_classname.toCharArray();
        char[] chars2 = ac2_classname.toCharArray();

        char[] chars1_lowercase = ac1_classname.toLowerCase().toCharArray();
        char[] chars2_lowercase = ac2_classname.toLowerCase().toCharArray();

        for( int i = 0; i<size; i++ ){
            if ( chars1_lowercase[i] > chars2_lowercase[i] )
                return true;
            else if ( chars1_lowercase[i] < chars2_lowercase[i] )
                return false;
            else if ( chars1_lowercase[i] == chars2_lowercase[i] ){
                if( chars1[i] > chars2[i] )
                    return true;
                else if( chars1[i] < chars2[i] )
                    return false;

            }
        }

        if( size1 > size2 )
            return true;
        else return false;
        }catch(Exception e){
            System.out.println("error - " + e.toString() );
            System.out.println();
            
        }
        return false;
    }
}
