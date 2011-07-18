package de.gaalop.tba.cfgImport.optimization.maxima;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author christian
 */
public class ProcessBuilderMaximaConnection implements MaximaConnection {

    public String commandMaxima;

    public static final String CMD_MAXIMA_LINUX = "/usr/bin/maxima";

    public ProcessBuilderMaximaConnection(String commandMaxima) {
        this.commandMaxima = commandMaxima;
    }

    @Override
    public MaximaOutput optimizeWithMaxima(MaximaInput input) {
        try {
            File tmpFile = File.createTempFile("tbaMaxima", ".txt");

            PrintWriter out = new PrintWriter(tmpFile);
            for (String line: input)
                out.println(line);
            out.close();

            MaximaOutput output = new MaximaOutput();


            ProcessBuilder builder = new ProcessBuilder(commandMaxima, "-b", tmpFile.getAbsolutePath());
            Process p = builder.start();

            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = b.readLine()) != null) {
                output.add(line);
            }

            b.close();

            tmpFile.delete();


            return output;
        } catch (IOException ex) {
            Logger.getLogger(ProcessBuilderMaximaConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        
    }
}