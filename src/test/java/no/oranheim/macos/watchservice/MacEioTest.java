package no.oranheim.macos.watchservice;

import no.oranheim.macos.watchservice.eio.FileHelper;
import no.oranheim.macos.watchservice.eio.FileManager;
import no.oranheim.macos.watchservice.osx.MacFileSystem;
import no.oranheim.macos.watchservice.osx.MacFileSystemProvider;
import no.oranheim.macos.watchservice.osx.MacPath;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class MacEioTest {
    @Test
    public void testSetup() {
        try {
            // General setup tests
            assertEquals("correct filesystem class", MacFileSystem.class, FileSystems.getDefault().getClass());
            assertEquals("correct provider class", MacFileSystemProvider.class, FileSystems.getDefault().provider().getClass());
            assertEquals("correct path class", MacPath.class, FileSystems.getDefault().getPath("build.xml").getClass());
        } catch (Throwable tossed) {
            fail(tossed.toString());
        }
    }

    @Test
    public void testManager() {
        try {
            File f = new File("build.xml");
            Path p = f.toPath();
            int i_type = FileManager.OSTypeToInt("TEXT");
            int i_creator = FileManager.OSTypeToInt("ttxt");
            FileManager.setFileTypeAndCreator(p.toString(), i_type, i_creator);
            assertEquals(i_creator, FileManager.getFileCreator(p.toString()));
            assertEquals(i_type, FileManager.getFileType(p.toString()));
            int i_sfri = FileManager.OSTypeToInt("sfri");
            FileManager.setFileCreator(p.toString(), i_sfri);
            assertEquals(i_sfri, FileManager.getFileCreator(p.toString()));
            FileManager.setFileCreator(p.toString(), i_creator);
            f = new File(FileManager.findFolder(FileManager.kUserDomain, FileManager.OSTypeToInt("asup")));
            assertTrue(f.exists());
        } catch (IOException ioex) {
            fail(ioex.toString());
        }
    }

    private static String getOSType(int ostype) {
        byte[] b = new byte[4];
        pokeInt(b, 0, ostype);
        return new String(b);
    }

    private static final void pokeInt(byte[] a, int index, int i) {
        a[index] = (byte) (i >> 24);
        a[index + 1] = (byte) (i >> 16);
        a[index + 2] = (byte) (i >> 8);
        a[index + 3] = (byte) (i);
    }

    @Test
    public void testHelper() {
        System.out.println(FileHelper.getTRZViews());
    }
}
