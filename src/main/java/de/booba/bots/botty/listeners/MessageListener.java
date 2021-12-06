package de.booba.bots.botty.listeners;


import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

public class MessageListener extends ListenerAdapter {

    public static Path path_file_with_cords = Path.of("C:\\Users\\jkl\\IdeaProjects\\botty\\file_with_cords.txt");
    public static List<String> lines;


    private EmbedBuilder cords_embed_builder = new EmbedBuilder();
    private static HashMap<String, String> cords = new HashMap<>();
    private static ArrayList<String> cords_on_ArrayList = new ArrayList<>();
    private static String description;

    public static MessageChannel channel;
    public static String msg;

    /**
     * This is the method where the program starts.
     */
    public static void main(String[] args) throws InterruptedException, LoginException, FileNotFoundException {
        File file = new File("C:\\Users\\jkl\\IdeaProjects\\botty\\token.txt");
        Scanner scanner = new Scanner(file);

        String token = scanner.nextLine();


        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new MessageListener())
                .build();
        jda.awaitReady();
        System.out.println("Finished Building JDA!");

    }

    /**
     * NOTE THE @Override!
     * This method is actually overriding a method in the ListenerAdapter class! We place an @Override annotation
     * right before any method that is overriding another to guarantee to ourselves that it is actually overriding
     * a method from a super class properly. You should do this every time you override a method!
     * <p>
     * As stated above, this method is overriding a hook method in the
     * {@link net.dv8tion.jda.api.hooks.ListenerAdapter ListenerAdapter} class. It has convenience methods for all JDA events!
     * Consider looking through the events it offers if you plan to use the ListenerAdapter.
     * <p>
     * In this example, when a message is received it is printed to the console.
     *
     * @param event An event containing information about a {@link net.dv8tion.jda.api.entities.Message Message} that was
     *              sent in a channel.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        User author = event.getAuthor();
        Message message = event.getMessage();
        channel = event.getChannel();

        msg = message.getContentDisplay();


        if (event.isFromType(ChannelType.TEXT)) {
            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            String name;
            if (message.isWebhookMessage()) {
                name = author.getName();
            } else {
                name = member.getEffectiveName();
            }

            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
        } else if (event.isFromType(ChannelType.PRIVATE)) {

            PrivateChannel privateChannel = event.getPrivateChannel();

            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }

        if (msg.equals("!ping")) {
            channel.sendMessage("Pong!").queue();

        } else if (msg.startsWith("!ban")) {

            if (message.getMentionedUsers().isEmpty()) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder
                        .setTitle("Botty")
                        .setDescription("Error: No User provided")
                        .setColor(Color.GREEN);

                event.getMessage().reply(new MessageBuilder()
                        .setEmbeds(embedBuilder.build()).build()).queue();

                event.getChannel().deleteMessageById(event.getMessageId()).queue();


            } else {
                Guild guild = event.getGuild();
                Member selfMember = guild.getSelfMember();


                if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder
                            .setTitle("Botty")
                            .setDescription("Error: You don't have permission")
                            .setColor(Color.GREEN);

                    event.getMessage().reply(new MessageBuilder()
                            .setEmbeds(embedBuilder.build()).build()).queue();

                    event.getChannel().deleteMessageById(event.getMessageId()).queue();

                    return;
                }


                List<Member> mentionedMembers = event.getMessage().getMentionedMembers();


                for (int i = 0; i < mentionedMembers.size(); i++) {

                    if (!selfMember.canInteract(mentionedMembers.get(i))) {

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder
                                .setTitle("Botty")
                                .setDescription("Error: You don't have any permission")
                                .setColor(Color.GREEN);

                        event.getMessage().reply(new MessageBuilder()
                                .setEmbeds(embedBuilder.build()).build()).queue();

                        event.getChannel().deleteMessageById(event.getMessageId()).queue();
                    } else {
                        mentionedMembers.get(i).ban(0).queue();

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder
                                .setTitle("Botty")
                                .setDescription("Banned successfully!")
                                .setColor(Color.GREEN);

                        event.getMessage().reply(new MessageBuilder()
                                .setEmbeds(embedBuilder.build()).build()).queue();

                        event.getChannel().deleteMessageById(event.getMessageId()).queue();
                    }
                }
            }
        } else if (msg.startsWith("!kick")) {


            if (message.getMentionedUsers().isEmpty()) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder
                        .setTitle("Botty")
                        .setDescription("Error: No User provided")
                        .setColor(Color.GREEN);

                event.getMessage().reply(new MessageBuilder()
                        .setEmbeds(embedBuilder.build()).build()).queue();

                event.getChannel().deleteMessageById(event.getMessageId()).queue();


            } else {
                Guild guild = event.getGuild();
                Member selfMember = guild.getSelfMember();


                if (!selfMember.hasPermission(Permission.KICK_MEMBERS)) {

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder
                            .setTitle("Botty")
                            .setDescription("Error: You don't have permission")
                            .setColor(Color.GREEN);

                    event.getMessage().reply(new MessageBuilder()
                            .setEmbeds(embedBuilder.build()).build()).queue();

                    event.getChannel().deleteMessageById(event.getMessageId()).queue();

                    return;
                }


                List<Member> mentionedMembers = event.getMessage().getMentionedMembers();


                for (int i = 0; i < mentionedMembers.size(); i++) {

                    if (!selfMember.canInteract(mentionedMembers.get(i))) {

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder
                                .setTitle("Botty")
                                .setDescription("Error: You don't have any permission")
                                .setColor(Color.GREEN);

                        event.getMessage().reply(new MessageBuilder()
                                .setEmbeds(embedBuilder.build()).build()).queue();

                        event.getChannel().deleteMessageById(event.getMessageId()).queue();
                    } else {
                        mentionedMembers.get(i).kick().queue();

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder
                                .setTitle("Botty")
                                .setDescription("Kicked successfully!")
                                .setColor(Color.GREEN);

                        event.getMessage().reply(new MessageBuilder()
                                .setEmbeds(embedBuilder.build()).build()).queue();

                        event.getChannel().deleteMessageById(event.getMessageId()).queue();
                    }
                }}
            }else if (msg.startsWith("!unban")){



            if (message.getMentionedUsers().isEmpty()) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder
                        .setTitle("Botty")
                        .setDescription("Error: No User provided")
                        .setColor(Color.GREEN);

                event.getMessage().reply(new MessageBuilder()
                        .setEmbeds(embedBuilder.build()).build()).queue();

                event.getChannel().deleteMessageById(event.getMessageId()).queue();


            } else {
                Guild guild = event.getGuild();
                Member selfMember = guild.getSelfMember();


                if (!selfMember.hasPermission(Permission.KICK_MEMBERS)) {

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder
                            .setTitle("Botty")
                            .setDescription("Error: You don't have permission")
                            .setColor(Color.GREEN);

                    event.getMessage().reply(new MessageBuilder()
                            .setEmbeds(embedBuilder.build()).build()).queue();

                    event.getChannel().deleteMessageById(event.getMessageId()).queue();

                    return;
                }


                List<Member> mentionedMembers = event.getMessage().getMentionedMembers();


                for (int i = 0; i < mentionedMembers.size(); i++) {

                    if (!selfMember.canInteract(mentionedMembers.get(i))) {

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder
                                .setTitle("Botty")
                                .setDescription("Error: You don't have any permission")
                                .setColor(Color.GREEN);

                        event.getMessage().reply(new MessageBuilder()
                                .setEmbeds(embedBuilder.build()).build()).queue();

                        event.getChannel().deleteMessageById(event.getMessageId()).queue();
                    } else {
                        mentionedMembers.get(i).ban(0);

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder
                                .setTitle("Botty")
                                .setDescription("Unbanned successfully!")
                                .setColor(Color.GREEN);

                        event.getMessage().reply(new MessageBuilder()
                                .setEmbeds(embedBuilder.build()).build()).queue();

                        event.getChannel().deleteMessageById(event.getMessageId()).queue();
                    }
                }
            }


        } else if (msg.startsWith("!cords_add")) {

            String removed_command = MessageListener.msg.replace("!cords_add", "").substring(1);
            List<String> split_command = new ArrayList(Arrays.asList(removed_command.split(":")));

            cords.put(split_command.get(0).replace(":", ""), split_command.get(1).substring(1));
            cords_on_ArrayList.add(split_command.get(0).replace(":", "") + split_command.get(1).substring(1));


            try {
                lines = Files.readAllLines(path_file_with_cords, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String allines = "";

            for (int i = 0; i < lines.size(); i++) {
                allines = allines + lines.get(i) + "\n";
            }


            try {
                Files.write(path_file_with_cords, Collections.singleton(allines + "Name: " + split_command.get(0) + "\t" +
                        "Coordinates" + split_command.get(1) + "\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }


            cords_embed_builder
                    .setColor(Color.GREEN)
                    .setTitle("Botty")
                    .setDescription("Name of the place: " + split_command.get(0).replace(":", "") + "\n" +
                            "its Coordinates" + split_command.get(1).substring(1));


            event.getMessage().reply(new MessageBuilder()
                    .setEmbeds(cords_embed_builder.build()).build()).queue();

            event.getChannel().deleteMessageById(event.getMessageId()).queue();

            System.out.println(MessageListener.msg.replace("!cords_add", "").substring(1) + " has been added!");

        } else if (msg.equalsIgnoreCase("!cords_all")) {

            cords_embed_builder
                    .setColor(Color.GREEN)
                    .setTitle("Botty");


            try {
                lines = Files.readAllLines(path_file_with_cords, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String allines = "";

            for (int i = 0; i < lines.size(); i++) {
                allines = allines + lines.get(i) + "\n";
            }

            System.out.println(allines);
            System.out.println(lines);
            cords_embed_builder.setDescription(allines);

            event.getMessage().reply(new MessageBuilder()
                    .setEmbeds(cords_embed_builder.build()).build()).queue();

            event.getChannel().deleteMessageById(event.getMessageId()).queue();


        } else if (msg.equals("!hook")) {

            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("Hook")
                    .setDescription("you hooked me")
                    .setColor(Color.RED);

            event.getMessage().reply(new MessageBuilder()
                    .setEmbeds(builder.build()).build()).queue();
        } else if (msg.equals("!help")) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Botty")
                    .setDescription("!hook, !ping - zum rumspielen \n !cords_add -Koordinaten hinzufuegen \n cords_all -Koordinaten anzeigen ")
                    .setColor(Color.GREEN);

            event.getMessage().reply(new MessageBuilder()
                    .setEmbeds(builder.build()).build()).queue();

            event.getChannel().deleteMessageById(event.getMessageId()).queue();

        } else if (msg.startsWith("!cords_find")) {

            String search = msg.replace("cords_find", "").substring(1);

            String results = "";

            try {
                lines = Files.readAllLines(path_file_with_cords);

                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains(search)) {
                        results = results + lines.get(i) + "\n";
                    }
                }

                EmbedBuilder builder = new EmbedBuilder();
                builder.setColor(Color.GREEN)
                        .setTitle("Botty")
                        .setDescription(results);

                event.getMessage().reply(new MessageBuilder()
                        .setEmbeds(builder.build()).build()).queue();

                event.getChannel().deleteMessageById(event.getMessageId()).queue();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

