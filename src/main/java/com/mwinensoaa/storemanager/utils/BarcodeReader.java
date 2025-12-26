package com.mwinensoaa.storemanager.utils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class BarcodeReader {



    private static void setOpenCVNativeLibraryPath(){
        // Set the path to the directory containing the native library
        String opencvLibPath = GeneralUtils.getOpenCVNativeLib().getPath();
        System.setProperty("java.library.path", opencvLibPath);
        // The following lines are necessary to reset the ClassLoader's library path cache
        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Load the OpenCV native library
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }


    public static void readBarcode(){
        try {
            // Path to the image file containing the barcode
            File file = new File("path/to/barcode/image.png");

            // Read the image file into a BufferedImage
            BufferedImage bufferedImage = ImageIO.read(file);

            // Convert the BufferedImage to a BinaryBitmap
            BufferedImageLuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

            // Decode the barcode using ZXing
            Result result = new MultiFormatReader().decode(binaryBitmap);

            // Print the decoded barcode text
            System.out.println("Barcode text: " + result.getText());
        } catch (IOException e) {
            System.err.println("Error reading the image file: " + e.getMessage());
        } catch (NotFoundException e) {
            System.err.println("No barcode found in the image: " + e.getMessage());
        }
    }



    public void processBarcodeImage(){
        // Open the default camera
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            return;
        }

        Mat frame = new Mat();
        while (true) {
            // Capture a frame from the camera
            if (camera.read(frame)) {
                // Convert the frame to a BufferedImage
                BufferedImage bufferedImage = matToBufferedImage(frame);

                if (bufferedImage != null) {
                    try {
                        // Convert the BufferedImage to a BinaryBitmap
                        BufferedImageLuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
                        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

                        // Decode the barcode using ZXing
                        Result result = new MultiFormatReader().decode(binaryBitmap);

                        // Print the decoded barcode text
                        System.out.println("Barcode text: " + result.getText());
                        break;
                    } catch (NotFoundException e) {
                        // No barcode found in the frame
                        System.out.println("No barcode found in the frame.");
                    }
                }
            }
        }

        // Release the camera
        camera.release();
    }

    private static BufferedImage matToBufferedImage(Mat mat) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(byteArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }







}
