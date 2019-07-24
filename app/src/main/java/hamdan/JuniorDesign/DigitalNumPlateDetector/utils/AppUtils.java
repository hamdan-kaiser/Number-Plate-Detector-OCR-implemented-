package hamdan.JuniorDesign.DigitalNumPlateDetector.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppUtils {

    public static void showToast(Context context, String text, boolean isLong) {
        Toast.makeText(context, text, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static List<Bitmap> renderToBitmap(Context context, File pdfFile) {
        Bitmap bi;
        List<Bitmap> images = new ArrayList<>();
        /*try {
            byte[] decode = IOUtils.toByteArray(inStream);
            ByteBuffer buf = ByteBuffer.wrap(decode);

            PDFFile pdfFile = new PDFFile(buf);
            PDFPage mPdfPage = pdfFile.getPage(0, true);
            Log.e("TESTING", "" + mPdfPage.getHeight());
            float width = mPdfPage.getWidth();
            float height = mPdfPage.getHeight();
            bi = mPdfPage.getImage(Math.round(width), Math.round(height), null, true, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bi.compress(Bitmap.CompressFormat.PNG, 100, stream);
            images.add(bi);

            *//*for (int i = 0; i < pdfFile.getNumPages(); i++) {
                PDFPage mPdfPage = new PDFFile(buf).getPage(i, true);
                float width = mPdfPage.getWidth();
                float height = mPdfPage.getHeight();
                RectF rect = new RectF(0, 0, (int) mPdfPage.getBBox().width(),
                        (int) mPdfPage.getBBox().height());
                bi = mPdfPage.getImage((int) (width), (int) (height), rect, true, true);
                images.add(bi);
            }*//*

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                // do nothing because the stream has already been closed
            }
        }*/
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));

            Bitmap bitmap;
            final int pageCount = renderer.getPageCount();
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = renderer.openPage(i);

                int width = Math.round(page.getWidth());
                int height = Math.round(page.getHeight());
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                images.add(bitmap);

                // close the page
                page.close();

            }

            // close the renderer
            renderer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return images;
    }

}
