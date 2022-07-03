package com.company.service;

import com.company.dto.AttachDTO;
import com.company.entity.AttachEntity;
import com.company.exps.ItemNotFoundEseption;
import com.company.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Service
public class AttachService {

    @Value("${attach.folder}")
    private String attachFolder;


    @Value("${server.url}")
    private String serverUrl;

    @Autowired
    private AttachRepository attachRepository;

   /* private final String attachFolder = "attache/";
    private final String serUrl = "http://localhost:8082/";*/

    public AttachDTO saveToSystem(MultipartFile file) {
        try {
            // zari.jpg
            String pathFolder = getYmDString(); // 2022/06/20
            String extension = getExtension(file.getOriginalFilename()); // jpg

            AttachEntity attachEntity = new AttachEntity();
            attachEntity.setOrginalName(file.getOriginalFilename());
            attachEntity.setExtensional(extension);
            attachEntity.setSize(file.getSize());
            attachEntity.setPath(pathFolder);
            attachRepository.save(attachEntity);

            String fileName = attachEntity.getId() + "." + extension; //  asdas-dasdas-dasdasd-adadsd.jpg

            File folder = new File(attachFolder + pathFolder); // attaches/2022/06/20
            if (!folder.exists()) {
                folder.mkdirs();
            }

            byte[] bytes = file.getBytes();
            // attaches/2022/06/20/asdas-dasdasd-asdas0asdas.jpg
            Path path = Paths.get(attachFolder + pathFolder + "/" + attachEntity.getId() + "." + extension);
            Files.write(path, bytes);

            AttachDTO attachDTO=new AttachDTO();
            attachDTO.setUrl(serverUrl+"attache/open/" +attachEntity.getId());

            return attachDTO;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



/*    public String saveToSystem(MultipartFile file) {
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get("attaches/" + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    //fuli
/*    public String saveToSystem(MultipartFile file) {
        try {
            // zari.jpg
            // asdas-dasdas-dasdasd-adadsd.jpg
            String uuid = UUID.randomUUID().toString();
            String extension = getExtension(file.getOriginalFilename());
            String fileName = uuid + "." + extension;
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get("attaches/" +fileName);
            Files.write(path, bytes);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/

 /*   public byte[] loadImage(String fileName) {
        byte[] imageInByte;

        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File("attaches/" + fileName));
        } catch (Exception e) {
            return new byte[0];
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(originalImage, "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageInByte;
    }
*/
    public byte[] open_general(String id) {
        byte[] data;

        Optional<AttachEntity> attach = attachRepository.findById(id);
        if (attach.isEmpty()) {
            throw new ItemNotFoundEseption("not found");
        }

        AttachEntity attachEntity = attach.get();

        try {
            // fileName -> zari.jpg
//            String path = "attaches/" + entity.getPath() + "/" + entity.getId() + "." + entity.getExtensional();
            String path = attachFolder + attachEntity.getPath() + "/" + attachEntity.getId() + "." + attachEntity.getExtensional();
            Path file = Paths.get(path);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public Resource download(String id) {

        Optional<AttachEntity> attach = attachRepository.findById(id);
        if (attach.isEmpty()){
            throw new ItemNotFoundEseption("not found");
        }

        AttachEntity attachEntity = attach.get();

        try {
            String path = attachFolder + attachEntity.getPath() + "/" + attachEntity.getId() + "." + attachEntity.getExtensional();

            Path file = Paths.get(path);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public String getYmDString() {

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2022/04/23
    }

    public String getTmDasUrlLink(String folder) { //  2022/06/20
        String[] arr = folder.split("/");
        return arr[0] + "_" + arr[1] + "_" + arr[2];
    }

    public String getFolderPathFromUrl(String url) { // 2022_6_20_f978a682-a357-4eaf-ac18-ec9482a4e58b.jpg
        String[] arr = url.split("_");
        return arr[0] + "/" + arr[1] + "/" + arr[2] + "/" + arr[3];
        // 2022/06/20/f978a682-a357-4eaf-ac18-ec9482a4e58b.jpg
    }

    public String delete(String id) {

        Optional<AttachEntity> attach = attachRepository.findById(id);
        if (attach.isEmpty()){
            throw new ItemNotFoundEseption("not found");
        }

        AttachEntity attachEntity = attach.get();
        attachRepository.deleteById(id);

        String path = attachFolder + attachEntity.getPath() + "/" + attachEntity.getId() + "." + attachEntity.getExtensional();

        try {
            Files.delete(Path.of(path));
        }catch (Exception e){
            e.printStackTrace();
        }

        return "succsess";
    }


    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundEseption("attach not found ");
        });
    }


    public byte[] loadImage(String id) {
        byte[] imageInByte;
        String path = getFileFullPath(get(id));

        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File(path));
        } catch (Exception e) {
            return new byte[0];
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(originalImage, "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageInByte;
    }

    private String getFileFullPath(AttachEntity entity) {
        return attachFolder + entity.getPath() + "/" + entity.getId() + "." + entity.getExtensional();
    }

    public PageImpl paginationAttach(int page, int size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AttachEntity> all = attachRepository.findAll(pageable);

        List<AttachDTO> dtoList = new LinkedList<>();

        all.getContent().forEach(attachEntity -> {
            AttachDTO dto = new AttachDTO();
            dto.setId(attachEntity.getId());
            dto.setOrginalName(attachEntity.getOrginalName());
            dto.setExtensional(attachEntity.getExtensional());
            dto.setPath(attachEntity.getPath());
            dtoList.add(dto);
        });

        return new PageImpl(dtoList,pageable, all.getTotalElements());
    }

    public boolean existByPhoto(String id){
        boolean exists = attachRepository.existsById(id);
        if (exists){
            return true;
        }
        return false;
    }

}
