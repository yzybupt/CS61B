public class ImageParamsGet {
    private final double ROOT_ULLAT = 37.892195547244356;
    private final double ROOT_ULLON = -122.2998046875;
    private final double ROOT_LRLAT = 37.82280243352756;
    private final double ROOT_LRLON = -122.2119140625;
    private final double FEET_PER_DEGREE = 288200.0;
    private double[] results;

    public ImageParamsGet (int depth, int x, int y){
        String newName = convertFilenameToOldStyle(depth, x, y);
        imagesConverter(newName);
    }

    public double[] getParams () {
        return results;
    }


    private void imagesConverter (String filename) {
        results = new double[5];
        results[0] = Ullon(filename);
        results[1] = Ullat(filename);
        results[2] = Lrlon(filename);
        results[3] = Lrat(filename);
        results[4] = (results[2] - results[0]) / 256;
    }

    private double Ullon(String filename) {
        if (filename.equals("root.png")) {
            return ROOT_ULLON;
        }
        double lonDelta = (ROOT_LRLON - ROOT_ULLON) / 2;
        double lon = ROOT_ULLON;

        for (int i = 0; i < filename.length(); i += 1) {
            char c = filename.charAt(i);
            /* Right half. */
            if (c == '2' || c == '4') {
                lon = lon + lonDelta;
            }
            /* Quit once you reach the extension. */
            if (c == '.') {
                break;
            }
            lonDelta = lonDelta / 2;
        }
        return lon;
    }

    private double Ullat(String filename) {
        if (filename.equals("root.png")) {
            return ROOT_ULLAT;
        }
        double latDelta = (ROOT_ULLAT - ROOT_LRLAT) / 2;
        double lat = ROOT_ULLAT;

        for (int i = 0; i < filename.length(); i += 1) {
            char c = filename.charAt(i);
            /* Bottom half. */
            if (c == '3' || c == '4') {
                lat = lat - latDelta;
            }
            /* Quit once you reach the extension. */
            if (c == '.') {
                break;
            }
            latDelta = latDelta / 2;
        }
        return lat;
    }

    private double Lrlon(String filename) {
        if (filename.equals("root.png")) {
            return ROOT_LRLON;
        }
        double lonDelta = (ROOT_LRLON - ROOT_ULLON) / 2;
        double lon = ROOT_LRLON;

        for (int i = 0; i < filename.length(); i += 1) {
            char c = filename.charAt(i);
            /* Left half. */
            if (c == '1' || c == '3') {
                lon = lon - lonDelta;
            }
            /* Quit once you reach the extension. */
            if (c == '.') {
                break;
            }
            lonDelta = lonDelta / 2;
        }
        return lon;
    }

    private double Lrat(String filename) {
        if (filename.equals("root.png")) {
            return ROOT_LRLAT;
        }
        double latDelta = (ROOT_ULLAT - ROOT_LRLAT) / 2;
        double lat = ROOT_LRLAT;

        for (int i = 0; i < filename.length(); i += 1) {
            char c = filename.charAt(i);
            /* Top half. */
            if (c == '1' || c == '2') {
                lat = lat + latDelta;
            }
            /* Quit once you reach the extension. */
            if (c == '.') {
                break;
            }
            latDelta = latDelta / 2;
        }
        return lat;
    }

    private String convertFilenameToOldStyle(int depth, int x, int y) {
        String s = "";
        char c = '1';
        for (int i = 0; i < depth; i++) {
            int k = Integer.valueOf(String.valueOf((int) c));
            String a = "" + (char) (k + (x & 1) + 2 * (y & 1));
            x >>= 1;
            y >>= 1;
            s = a + s;
        }


        return s;
    }

    public static void main(String[] args) {
        ImageParamsGet image = new ImageParamsGet(7,3,7);
        System.out.println(image.getParams()[0]);
        System.out.println(image.getParams()[1]);
        System.out.println(image.getParams()[2]);
        System.out.println(image.getParams()[3]);
        //System.out.println(image.getParams()[4]);
    }

}
