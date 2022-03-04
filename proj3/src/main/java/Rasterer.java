
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    double[] resolutionLevel = new double[8];

    public Rasterer() {
        ImageParamsGet image = new ImageParamsGet(0, 0, 0);
        resolutionLevel[0] = image.getParams()[4];
        for (int i = 1; i <= 7; i++) {
            resolutionLevel[i] = resolutionLevel[i - 1] / 2;
        }

    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println(params);
        double x0 = params.get("ullon");
        double y0 = params.get("ullat");
        double x3 = params.get("lrlon");
        double y3 = params.get("lrlat");
        int depth = 0;
        int filezuoshangx = 0;
        int filezuoshangy = 0;
        int fileyouxiax = 0;
        int fileyouxiay = 0;
        Map<String, Object> results = new HashMap<>();

        //firstly, exclude the case where query box can not form  a rectangular
        if (x3 <= x0 || y0 <= y3) {
            results.put("query_success", false);
            return results;
        }

        //secondly, exclude the case where query box has no overlap with world
        if (!checkWithinWorld(x0, y0) && !checkWithinWorld(x3, y3) && !checkWithinWorld(x0, y3)
                && !checkWithinWorld(x3, y0)) {
            if (!(ROOT_ULLON <= x3 && ROOT_ULLON >= x0)) {
                results.put("query_success", false);
                return results;
            }
        }

        //thirdly, match the desired depth with resolution
        double queryResolution = (x3 - x0) / params.get("w");
        if (queryResolution <= resolutionLevel[7]) {
            depth = 7;
        } else if (queryResolution >= resolutionLevel[0]) {
            depth = 0;
        } else {
            for (int i = 0; i <= 6; i++) {
                if (resolutionLevel[i] >= queryResolution && resolutionLevel[i + 1] < queryResolution) {
                    depth = i + 1;
                    break;
                }
            }

        }

        //look for range of zuoshangx
        if (x0 < ROOT_ULLON) {
            filezuoshangx = 0;
        } else if (x0 > ROOT_LRLON) {
            filezuoshangx = (int) Math.pow(2.0,(double) depth) - 1;
        } else {
            for (int i = 0; i < (int) Math.pow(2.0,(double) depth); i++) {
                ImageParamsGet image = new ImageParamsGet(depth, i,0);
                if (x0 < image.getParams()[2] && x0 > image.getParams()[0]) {
                    filezuoshangx = i;
                    break;
                }
            }
        }



        //look for range of zuoshangy
        if (y0 <= ROOT_LRLAT) {
            filezuoshangy = (int) Math.pow(2.0,(double) depth) - 1;
        } else if (y0 >= ROOT_ULLAT) {
            filezuoshangy = 0;
        } else {
            for (int i = 0; i < (int) Math.pow(2.0,(double) depth); i++) {
                ImageParamsGet image = new ImageParamsGet(depth,0, i);
                if (y0 < image.getParams()[1] && y0 > image.getParams()[3]) {
                    filezuoshangy = i;
                    break;
                }

            }
        }

        //look for range of youxiax
        if (x3 < ROOT_ULLON) {
            fileyouxiax = 0;
        } else if (x3 > ROOT_LRLON) {
            fileyouxiax = (int) Math.pow(2.0,(double) depth) - 1;
        } else {
            for (int i = 0; i < (int) Math.pow(2.0, (double) depth); i++) {
                ImageParamsGet image = new ImageParamsGet(depth, i, 0);
                if (x3 < image.getParams()[2] && x3 > image.getParams()[0]) {
                    fileyouxiax = i;
                    break;
                }

            }
        }

        //look for range of youxiay
        if (y3 <= ROOT_LRLAT) {
            fileyouxiay = (int) Math.pow(2.0,(double) depth) - 1;
        } else if (y3 >= ROOT_ULLAT) {
            fileyouxiay = 0;
        } else {
            for (int i = 0; i < (int) Math.pow(2.0, (double) depth); i++) {
                ImageParamsGet image = new ImageParamsGet(depth, 0, i);
                if (y3 < image.getParams()[1] && y3 > image.getParams()[3]) {
                    fileyouxiay = i;
                    break;
                }
            }
        }

        //search and combine the display images

        String[][] imageFiles = new String[Math.abs(filezuoshangy - fileyouxiay) + 1 ][Math.abs(fileyouxiax - filezuoshangx) + 1];
        for (int i = 0; i <= fileyouxiay - filezuoshangy; i++){
            for (int j = 0; j<= fileyouxiax - filezuoshangx; j++) {
                imageFiles[i][j] = "d" + depth + "_x" + (j + filezuoshangx)  + "_y" + (i + filezuoshangy) + ".png";
            }
        }

        /* @return A map of results for the front end as specified: <br>
         * "render_grid"   : String[][], the files to display. <br>
         * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
         * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
         * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
         * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
         * "depth"         : Number, the depth of the nodes of the rastered image <br>
         * "query_success" : Boolean, whether the query was able to successfully complete; don't
         *                    forget to set this to true on success! <br>
         */
        results.put("render_grid", imageFiles);
        ImageParamsGet image1 = new ImageParamsGet(depth, filezuoshangx, filezuoshangy);
        results.put("raster_ul_lon",image1.getParams()[0]);
        results.put("raster_ul_lat",image1.getParams()[1]);

        ImageParamsGet image2 = new ImageParamsGet(depth, fileyouxiax, fileyouxiay);
        results.put("raster_lr_lon",image2.getParams()[2]);
        results.put("raster_lr_lat",image2.getParams()[3]);

        results.put("depth", depth);
        results.put("query_success", true);



        return results;
    }


    private boolean checkWithinWorld (double x, double y) {
        if (x <= ROOT_LRLON && x >= ROOT_ULLON && y <= ROOT_ULLAT && y >= ROOT_LRLAT) {
            return true;
        } else {
            return false;
        }
    }

}
