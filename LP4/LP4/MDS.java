package LP4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

// If you want to create additional classes, place them in this file as subclasses of MDS

public class MDS {
    // Add fields of MDS here
    TreeMap<Long, Product> items;
    HashMap<Long, HashSet<Long>> desc_list;

    // Constructors
    public MDS() {

        items = new TreeMap<>();
        desc_list = new HashMap<>();
    }

    /*
     * Public methods of MDS. Do not change their signatures.
     * __________________________________________________________________ a.
     * Insert(id,price,list): insert a new item whose description is given in the
     * list. If an entry with the same id already exists, then its description and
     * price are replaced by the new values, unless list is null or empty, in which
     * case, just the price is updated. Returns 1 if the item is new, and 0
     * otherwise.
     */

    public int insert(long id, Money price, java.util.List<Long> list) {

        Product obj = items.get(id);
        int flag;

        // if the id is not present in items
        if (obj == null) {
            items.put(id, new Product(id, price, list));
            flag = 1;
        }

        // if this id already exists in items
        else {
        
            if (list.size() != 0) {
                System.out.println(id+" "+list);
                // as the description list is updated in this case, deleting ids from the list
                // which is mapped to this id in desc_list
                List<Long> description = obj.description;
                for (long desc : description) {
                    desc_list.get(desc).remove(id);
                }

                // updated the description list
                obj.description = list;
                obj.price = price;
            }

            flag = 0;
            obj.price = price;
        }

        // fetching the object associated with id if it already exists for updating
        // price and desc_list

        if (list.size()!=0){

            List<Long> description = items.get(id).description;
            if (description != null) {

                for (long desc : description) {

                    if (desc_list.get(desc) != null) {
                        desc_list.get(desc).add(id);
                    } else {
                        desc_list.put(desc, new HashSet<Long>());
                        desc_list.get(desc).add(id);
                    }
                }


            }
            //System.out.println(desc_list+" insert");
        }

        // if flag == 1, new item is added
        if (flag == 1) {
            return 1;
        }

        // function will return zero when flag is 0
        return 0;

    }

    // b. Find(id): return price of item with given id (or 0, if not found).
    public Money find(long id) {

        Product obj = items.get(id);

        if (obj != null) {
            return obj.price;
        }

        return new Money();
    }

    /*
     * c. Delete(id): delete item from storage. Returns the sum of the long ints
     * that are in the description of the item deleted, or 0, if such an id did not
     * exist.
     */
    public long delete(long id) {

        Product obj = items.get(id);
        if (obj != null) {

            long sum = 0;
            for (long desc : obj.description) {
                sum += desc;
            }

            for(long desc:obj.description){
                desc_list.get(desc).remove(id);
            }

            items.remove(id);

            return sum;
        }
        return 0;
    }

    /*
     * d. FindMinPrice(n): given a long int, find items whose description contains
     * that number (exact match with one of the long ints in the item's
     * description), and return lowest price of those items. Return 0 if there is no
     * such item.
     */
    public Money findMinPrice(long n) {

        HashSet<Long> ids = desc_list.get(n);
        if (ids != null) {
            //System.out.println(ids+" sexy");
            Money min_price = new Money(Long.MAX_VALUE, Integer.MAX_VALUE);
            for (Long id : ids) {
                Money price = items.get(id).price;
                if (min_price.compareTo(price) > 0) {
                    min_price = price;
                   // System.out.print(min_price.d);
                
                }
            }
            //System.out.println();    
            return min_price;
        }
        return new Money();
    }

    /*
     * e. FindMaxPrice(n): given a long int, find items whose description contains
     * that number, and return highest price of those items. Return 0 if there is no
     * such item.
     */
    public Money findMaxPrice(long n) {

        HashSet<Long> ids = desc_list.get(n);
        if (ids != null) {

            Money max_price = new Money();
            for (Long id : ids) {
                //System.out.println(id);
                //System.out.println(items);
                Money price = items.get(id).price;
                if (max_price.compareTo(price) < 0) {
                    max_price = price;
                }
            }
            return max_price;
        }
        return new Money();
    }

    /*
     * f. FindPriceRange(n,low,high): given a long int n, find the number of items
     * whose description contains n, and in addition, their prices fall within the
     * given range, [low, high].
     */
    public int findPriceRange(long n, Money low, Money high) {
        if (!desc_list.containsKey(n))
            return 0;
        int number_of_items = 0;
        HashSet<Long> ids = desc_list.get(n);
        for (long id : ids) {
            Product product = items.get(id);
            if ((product.price.compareTo(low) > 0 && product.price.compareTo(high) < 0)
                    || product.price.compareTo(low) == 0 || product.price.compareTo(high) == 0)
                number_of_items += 1;
        }
        return number_of_items;
    }

    /*
     * g. PriceHike(l,h,r): increase the price of every product, whose id is in the
     * range [l,h] by r%. Discard any fractional pennies in the new prices of items.
     * Returns the sum of the net increases of the prices.
     */
    public Money priceHike(long l, long h, double rate) {
        Map<Long, Product> sub = items.subMap(l, h+1);
        double netSum = 0;
        for (Map.Entry<Long, Product> entry : sub.entrySet()) {
            long key = entry.getKey();
            Product product = entry.getValue();
            long d = product.price.dollars();
            int c = product.price.cents();
            double oldPrice = (double) d + (double) c / 100;
            double newPrice = ((double) d + (double) c / 100) + ((double) d + (double) c / 100) * (rate / 100);
            double increase = newPrice - oldPrice;

            String doubleAsString = String.valueOf(newPrice);
            String[] temp = doubleAsString.split("\\.");
            String newDollarValue = temp[0];
            String newCentValue = temp[1];
            if(newCentValue.length()==1)
                newCentValue+="0";
            else
                newCentValue=newCentValue.substring(0, 2);
            
            product.price.d = Long.parseLong(newDollarValue);
            product.price.c = Integer.parseInt(newCentValue);
            
            items.get(key).price = product.price;

            doubleAsString = String.valueOf(increase);
            temp = doubleAsString.split("\\.");
            newDollarValue = temp[0];
            newCentValue = temp[1];
            if(newCentValue.length()==1)
                newCentValue+="0";
            else
                newCentValue=newCentValue.substring(0, 2);
            //System.out.println(newDollarValue+" "+newCentValue+" increase::::");
            netSum+=increase;
        }
        //System.out.println("netSum:"+netSum);
        String doubleAsString = String.valueOf(netSum);
            
        String[] temp = doubleAsString.split("\\.");
        String dv = temp[0];
        String cv = temp[1];
        if(cv.length()==1)
            cv+="0";
        else
            cv=cv.substring(0, 2);
        return new Money(dv+"."+cv);
    }

    /*
     * h. RemoveNames(id, list): Remove elements of list from the description of id.
     * It is possible that some of the items in the list are not in the id's
     * description. Return the sum of the numbers that are actually deleted from the
     * description of id. Return 0 if there is no such id.
     */
    public long removeNames(long id, java.util.List<Long> list) {
        if (!items.containsKey(id))
            return 0;
        Product product = items.get(id);
        long sum = 0;
        for (int i = 0; i < list.size(); i++) {
            if (product.description.contains(list.get(i))) {
                sum += list.get(i);
                int index = product.description.indexOf(list.get(i));
                product.description.remove(index);
            }
        }
        return sum;
    }

    public static class Product {
        long id;
        Money price;
        List<Long> description = new ArrayList<Long>();

        public Product(long id, Money price, List<Long> desc) {
            this.id = id;
            this.price = price;
            this.description = desc;
        }
    }

    // Do not modify the Money class in a way that breaks LP4Driver.java
    public static class Money implements Comparable<Money> {
        long d;
        int c;

        public Money() {
            d = 0;
            c = 0;
        }

        public Money(long d, int c) {
            this.d = d;
            this.c = c;
        }

        public Money(String s) {
            String[] part = s.split("\\.");
            int len = part.length;
            if (len < 1) {
                d = 0;
                c = 0;
            } else if (len == 1) {
                d = Long.parseLong(s);
                c = 0;
            } else {
                d = Long.parseLong(part[0]);
                c = Integer.parseInt(part[1]);
                if (part[1].length() == 1) {
                    c = c * 10;
                }
            }
        }

        public long dollars() {
            return d;
        }

        public int cents() {
            return c;
        }

        public int compareTo(Money other) { // Complete this, if needed

            if (other.d > this.d) {
                return -1;
            } else if (other.d < this.d) {
                return 1;
            } else {

                if (other.c > this.c) {
                    return -1;
                } else if (other.c < this.c) {
                    return 1;
                } else {
                    return 0;
                }
            }

        }

        public String toString() {
            if (c < 10)
                return d + ".0" + c;
            return d + "." + c;
        }
    }

    public static void main(String args[]) throws FileNotFoundException {
        Scanner sc;
        if (args.length > 0) {
            File file = new File(args[0]);
            sc = new Scanner(file);
        } else {
            sc = new Scanner(System.in);
        }
        // input = new BufferedReader(new FileReader(new File(finaName)));
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        int result = 0;
        MDS mds = new MDS();
        String line = null;
        int serialNumber = 0;

        while ((line = sc.nextLine()) != null) {
            if (line.trim().contains("End")) {
                break;
            }
            if (line.trim().indexOf('#') == 0)
                continue;
            line = line.replace("\t","");
            String[] words = line.split(" ");
            operation = words[0];
            
            switch (operation) {
            case "Insert": {
                long id = Long.parseLong(words[1]);
                Money money = new Money(words[2]);
                List<Long> desc_list = new ArrayList<>();
                for (int i = 3; i < words.length-1; i++) {
                    desc_list.add(Long.parseLong(words[i]));
                }
                int temp = mds.insert(id, money, desc_list);
                result +=temp;
                serialNumber++;
                System.out.println(serialNumber+"  "+operation+" "+temp+" "+result);
                break;
            }
            case "Find": {
                long id = Long.parseLong(words[1]);
                Money res = mds.find(id);
                result+=(int)Double.parseDouble(res.toString());
                serialNumber++;
                System.out.println(serialNumber+"  "+operation+"  "+(int)Double.parseDouble(res.toString())+"  "+result);
              
                break;
            }
            case "Delete": {
                long id = Long.parseLong(words[1]);
                long res = mds.delete(id);
                //System.out.println(res+" "+"Delete");
                
                result+=res;
                //System.out.println("result "+result);
                serialNumber++;
                System.out.println(serialNumber+"  "+operation+"  "+res+"  "+result);
              
                break;
            }
            case "FindMinPrice": {
                long id = Long.parseLong(words[1]);
                Money res = mds.findMinPrice(id);
                result+=(int)Double.parseDouble(res.toString());
                serialNumber++;
                System.out.println(serialNumber+"  "+operation+"  "+(int)Double.parseDouble(res.toString())+"  "+result);
              
                break;
            }
            case "FindMaxPrice": {
                long id = Long.parseLong(words[1]);
                Money res = mds.findMaxPrice(id);
                result+=(int)Double.parseDouble(res.toString());
                serialNumber++;
                System.out.println(serialNumber+"  "+operation+"  "+(int)Double.parseDouble(res.toString())+"  "+result);
              
                break;
            }
            case "FindPriceRange": {
                long n = Long.parseLong(words[1]);
                Money low = new Money(words[2]);
                Money high = new Money(words[3]);
                int res = mds.findPriceRange(n, low, high);
                result+=res;

                serialNumber++;
                System.out.println(serialNumber+"  "+operation+"  "+res+"  "+result);
               
                break;
            }
            case "PriceHike": {
                long l = Long.parseLong(words[1]);
                long h = Long.parseLong(words[2]);
                double rate = Double.parseDouble(words[3]);
                Money res = mds.priceHike(l, h, rate);
                result+=(int)Double.parseDouble(res.toString());
                serialNumber++;
                System.out.println(serialNumber+"  "+operation+"  "+(int)Double.parseDouble(res.toString())+"  "+result);
               
                break;
            }
            case "removeNames": {
                long id = Long.parseLong(words[1]);
                List<Long> desc_list = new ArrayList<>();
                for (int i = 2; i < words.length; i++) {
                    desc_list.add(Long.parseLong(words[i]));
                }
                long res = mds.removeNames(id, desc_list);
                result+=res;
                
                serialNumber++;
                System.out.println(serialNumber+"  "+operation+"  "+res+"  "+result);
              
                break;
            }

            }
        }
        System.out.println(result);
    }

}
