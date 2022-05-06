/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.cnab;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.lang.StringUtils.EMPTY;
import org.jrimum.texgit.FlatFile;
import org.jrimum.texgit.Record;
import org.jrimum.texgit.Texgit;
import org.jrimum.texgit.TexgitException;
import org.jrimum.utilix.ClassLoaders;
import org.jrimum.utilix.Collections;
import org.jrimum.utilix.Objects;
import org.jrimum.utilix.text.Strings;

public abstract class AbstractFlatFile {

    private String cfgFile;

    private FlatFile<Record> flatFile;

    protected static final String ARQUIVO_NULO = "Arquivo TXT a ser importado nulo!";

    protected AbstractFlatFile(String cfgFile) {
        init(cfgFile);
    }

    protected final void init(String cfgFile) {
        Strings.checkNotBlank(cfgFile, "Arquivo invalido!");
        this.cfgFile = cfgFile;
        configure();
    }

    protected final FlatFile<Record> getFlatFile() {
        return flatFile;
    }

    private void configure() {
        InputStream in = null;

        try {
            in = ClassLoaders.getResourceAsStream(cfgFile, this.getClass());
            File config = File.createTempFile(cfgFile, EMPTY);
            FileUtils.copyInputStreamToFile(in, config);
            flatFile = Texgit.createFlatFile(config);
        } catch (IOException | TexgitException e) {
            throw new IllegalStateException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        Objects.checkNotNull(flatFile, "NAO FOI POSSIVEL INICIALIZAR A LIB TEXGIT!");
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractFlatFile> T read(final List<String> lines) {
        Collections.checkNotEmpty(lines, "Linhas ausentes!");

        try {
            getFlatFile().read(lines);
        } catch (Exception e) {
        }

        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractFlatFile> T read(final File file) {
        Objects.checkNotNull(file, ARQUIVO_NULO);

        try {
            getFlatFile().read(FileUtils.readLines(file));
        } catch (IOException e) {
        }

        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractFlatFile> T read(final File file, String encoding) {
        Objects.checkNotNull(file, ARQUIVO_NULO);
        Strings.checkNotBlank(encoding, "Encoding inválido!");

        try {
            getFlatFile().read(FileUtils.readLines(file, encoding));
        } catch (IOException e) {
        }

        return (T) this;
    }

    public File write() throws IOException {
        Objects.checkNotNull(getFlatFile(), ARQUIVO_NULO);

        File f = File.createTempFile(this.getClass().getName() + "" + new Date().getTime(), "_jnfmtmp.txt");

        FileUtils.writeLines(f, getFlatFile().write());

        return f;
    }

    public File write(String encoding) throws IOException {
        if (getFlatFile() != null) {
            File f = File
                    .createTempFile(this.getClass().getName() + "" + new Date().getTime(), "_jnfmtmp.txt");
            FileUtils.writeLines(f, getFlatFile().write(), encoding);
            return f;
        } else {
            throw new IllegalArgumentException(new NullPointerException(ARQUIVO_NULO));
        }
    }

}
