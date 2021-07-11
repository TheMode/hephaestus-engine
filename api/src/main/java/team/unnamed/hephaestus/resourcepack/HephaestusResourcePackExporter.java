package team.unnamed.hephaestus.resourcepack;

import com.google.gson.Gson;
import team.unnamed.hephaestus.io.Streamable;
import team.unnamed.hephaestus.model.Model;
import team.unnamed.hephaestus.model.ModelBone;
import team.unnamed.hephaestus.model.ModelDescription;
import team.unnamed.hephaestus.resourcepack.java.JavaItem;
import team.unnamed.hephaestus.resourcepack.java.JavaModel;
import team.unnamed.hephaestus.serialize.GsonFactory;
import team.unnamed.hephaestus.io.Streams;
import team.unnamed.hephaestus.io.ZippedDataOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HephaestusResourcePackExporter
        implements ResourcePackExporter {

    private static final int PACK_FORMAT = 6;
    public static final String NAMESPACE = "hephaestus";
    private static final String PACK_METADATA = "{\n" +
            "  \"pack\": {\n" +
            "    \"pack_format\": " + PACK_FORMAT + ",\n" +
            "    \"description\": \"Hephaestus custom generated resource pack\"\n" +
            "  }\n" +
            "}";

    private final Gson gson = GsonFactory.createDefault();
    private final ModelGeometryTransformer transformer = new ModelGeometryTransformer();

    @Override
    public List<Model> export(OutputStream stream, List<Model> models) throws IOException {
        this.applyCustomModelData(models);

        try (ZippedDataOutputStream output = new ZippedDataOutputStream(stream, gson)) {

            // write the pack data
            output.startEntry("pack.mcmeta");
            output.writeString(PACK_METADATA);
            output.closeEntry();

            // write the resource pack icon
            output.startEntry("pack.png");
            InputStream iconPng = getClass().getClassLoader().getResourceAsStream("unnamed.png");
            if (iconPng != null) {
                Streams.pipe(iconPng, output);
            }
            output.closeEntry();

            List<JavaItem.Override> overrides = new ArrayList<>();

            for (Model model : models) {
                ModelDescription description = model.getGeometry().getDescription();
                String modelName = model.getName();

                for (Map.Entry<String, Streamable> texture : model.getTextures().entrySet()) {
                    String textureName = texture.getKey();
                    Streamable data = texture.getValue();

                    // write the texture
                    output.startEntry("assets/" + NAMESPACE + "/textures/" + modelName + "/" + textureName);
                    try (InputStream input = data.openIn()) {
                        Streams.pipe(input, output);
                    }
                    output.closeEntry();
                }
                // then write all the model bones
                for (ModelBone bone : this.transformer.getAllBones(model.getGeometry())) {
                    JavaModel javaModel = this.transformer.generateJavaModel(model, description, bone);

                    overrides.add(new JavaItem.Override(
                                    bone.getCustomModelData(),
                                    NAMESPACE + ":"
                                            + modelName
                                            + "/" + javaModel.getFileName()
                            )
                    );


                    output.startEntry(
                            "assets/" + NAMESPACE + "/models/"
                                    + modelName
                                    +  "/" + javaModel.getFileName()
                                    + ".json"
                    );
                    output.writeJson(javaModel);
                    output.closeEntry();
                }
            }

            output.startEntry("assets/minecraft/models/item/bone.json");
            output.writeJson(new JavaItem(overrides));
            output.closeEntry();
        }

        return models;
    }

    public void applyCustomModelData(List<Model> models) {
        int data = 1;
        for (Model model : models) {
            for (ModelBone bone : transformer.getAllBones(model.getGeometry())) {
                bone.setCustomModelData(data++);
            }
        }
    }

}