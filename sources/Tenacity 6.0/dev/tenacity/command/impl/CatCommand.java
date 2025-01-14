package dev.tenacity.command.impl;

import dev.tenacity.command.AbstractCommand;
import dev.tenacity.util.misc.ChatUtil;

public final class CatCommand extends AbstractCommand {

    public CatCommand() {
        super("cat", "Shows ascii art cat", ".cat list/aaah", 1);
    }

    @Override
    public void onCommand(final String[] arguments) {
        switch (arguments[0].toLowerCase()) {
            case "aaah": {
                // For chat
                // For chat
                // For chat
                // For chat
                // For chat
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠀⠀⠀⠀⠀⠀⠀⠀⢀⡀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⢀⣴⢟⣿⠇⢀⡄⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⢰⡇⠀⠀⠀⠀⢀⣿⣫⡾⢛⣴⢟⣷⠟⠁⠀⠀⠀⠀⠀⠀⢀⣤⡶⢞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣼⡇⠀⠀⠀⢠⡿⣱⡟⣱⡟⣽⠿⠁⠀⠀⠀⠀⠀⢀⣤⠾⣛⣵⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⡇⠀⠀⢠⡿⣱⣿⣿⢯⡾⠋⠀⠀⠀⠀⠀⢀⣴⠟⣡⣾⣯⡵⠖⢚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⢹⣇⠀⢀⣿⢣⣿⣿⢣⡿⠁⠀⠀⠀⠀⢀⣰⠟⣡⣾⢿⣋⡴⠞⠋⣁⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹⣜⣿⣆⣾⡇⣾⡿⣳⡟⠁⠀⠀⠀⠀⣠⡾⣣⡾⣫⣶⣿⣿⠶⢒⣉⣭⠵⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣴⠾⠛⠋⠉⠙⢻⣿⠿⠛⠛⠛⠛⣻⡶⠶⠟⠛⠒⠳⢾⣇⠠⠤⠒⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡶⠟⠉⠀⠀⠀⠀⠀⠀⠀⠉⠙⠒⠒⠶⠾⣅⠀⢀⣀⣀⠀⠀⠀⠹⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⠟⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣼⣿⣯⣯⣿⠿⣦⠀⠀⢻⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣴⡟⠁⠀⠀⠀⢀⣠⣴⣼⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⣟⠋⣛⠙⠳⣮⣳⣄⠈⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⡏⠀⠀⠀⠀⣴⣿⣿⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣷⣤⣙⢻⣥⠄⠀⢸⠋⠉⠀⣿⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣿⠀⠀⠀⠀⢰⣿⣿⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠃⠀⣈⣹⡿⢻⣿⣿⠀⠀⠀⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡟⠀⠀⠀⠀⣿⣿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⡟⠀⠀⠴⠖⠀⣾⣧⣿⠀⠀⠀⣿⢇⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⠃⠀⠀⠀⠠⢿⠛⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣿⡇⠀⠀⠴⠒⠀⢾⣽⡿⠀⠀⠀⡿⠿⣿⣤⣤⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡞⣾⣧⣤⠤⠶⠒⠀⠀⢹⣷⠀⠀⠀⠀⠸⠛⠉⠉⢩⣿⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⢿⣴⣇⣭⠗⠂⠀⠀⠀⠀⠈⣿⠀⠀⠀⠀⠀⠀⠀⢠⣿⢿⣷⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣿⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠏⠀⠙⠁⢀⣴⣶⣦⣀⠀⠀⠀⠘⣧⡀⠀⠀⠀⠀⡀⠀⢀⣿⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡾⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⡏⢀⣤⣴⣶⣿⣿⣿⣿⣿⣷⣄⠀⣠⣬⣿⣶⡶⠞⡻⢿⣷⣿⢿⣿⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⠀⠀⠀⣠⡾⠋⠀⠀⠀⠀⢀⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣷⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠋⠀⠀⠙⠿⣿⣿⣿⣿⣿⣾⡏⠻⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⠀⠀⠀⣀⣴⡾⢉⡤⠴⠒⠊⠉⣹⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣟⠉⠀⠀⠀⠀⠀⢀⣼⣿⣼⢿⠁⣼⡇⠀⠉⠻⣶⣀⠀⠀⠀⠀⠀⠀");
                ChatUtil.cat("⠀⠀⢀⣠⡾⠛⠁⠀⠀⠀⠀⠀⢀⣴⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣿⡛⢿⣿⣙⡋⠻⠟⣿⣤⣤⡤⠶⢖⣻⣿⣿⡯⠷⠟⠛⠋⠁⠀⠀⠀⠈⠙⢷⣤⣀⠀⠀⠀");
                ChatUtil.cat("⣠⣶⠿⠉⠀⠀⠀⠀⠀⠀⠀⠀⡼⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠳⢦⣍⣉⣉⣉⣍⣥⣴⠶⠾⠋⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠶⣶⣾⣿⡷⠀⠀");
                ChatUtil.cat("⠛⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠇⠀⢠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣿⡄⠀⠀");
                // For console
                // For console
                // For console
                // For console
                // For console
                // For console
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠀⠀⠀⠀⠀⠀⠀⠀⢀⡀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠁⠀⠀⠀⠀⠀⠀⢀⣴⢟⣿⠇⢀⡄⢀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⢰⡇⠀⠀⠀⠀⢀⣿⣫⡾⢛⣴⢟⣷⠟⠁⠀⠀⠀⠀⠀⠀⢀⣤⡶⢞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣼⡇⠀⠀⠀⢠⡿⣱⡟⣱⡟⣽⠿⠁⠀⠀⠀⠀⠀⢀⣤⠾⣛⣵⠞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⡇⠀⠀⢠⡿⣱⣿⣿⢯⡾⠋⠀⠀⠀⠀⠀⢀⣴⠟⣡⣾⣯⡵⠖⢚⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⢹⣇⠀⢀⣿⢣⣿⣿⢣⡿⠁⠀⠀⠀⠀⢀⣰⠟⣡⣾⢿⣋⡴⠞⠋⣁⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹⣜⣿⣆⣾⡇⣾⡿⣳⡟⠁⠀⠀⠀⠀⣠⡾⣣⡾⣫⣶⣿⣿⠶⢒⣉⣭⠵⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣴⠾⠛⠋⠉⠙⢻⣿⠿⠛⠛⠛⠛⣻⡶⠶⠟⠛⠒⠳⢾⣇⠠⠤⠒⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡶⠟⠉⠀⠀⠀⠀⠀⠀⠀⠉⠙⠒⠒⠶⠾⣅⠀⢀⣀⣀⠀⠀⠀⠹⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⠟⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣼⣿⣯⣯⣿⠿⣦⠀⠀⢻⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣴⡟⠁⠀⠀⠀⢀⣠⣴⣼⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⣟⠋⣛⠙⠳⣮⣳⣄⠈⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⡏⠀⠀⠀⠀⣴⣿⣿⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣷⣤⣙⢻⣥⠄⠀⢸⠋⠉⠀⣿⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣿⠀⠀⠀⠀⢰⣿⣿⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠃⠀⣈⣹⡿⢻⣿⣿⠀⠀⠀⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡟⠀⠀⠀⠀⣿⣿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⡟⠀⠀⠴⠖⠀⣾⣧⣿⠀⠀⠀⣿⢇⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⠃⠀⠀⠀⠠⢿⠛⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣿⡇⠀⠀⠴⠒⠀⢾⣽⡿⠀⠀⠀⡿⠿⣿⣤⣤⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡞⣾⣧⣤⠤⠶⠒⠀⠀⢹⣷⠀⠀⠀⠀⠸⠛⠉⠉⢩⣿⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⢿⣴⣇⣭⠗⠂⠀⠀⠀⠀⠈⣿⠀⠀⠀⠀⠀⠀⠀⢠⣿⢿⣷⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣿⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠏⠀⠙⠁⢀⣴⣶⣦⣀⠀⠀⠀⠘⣧⡀⠀⠀⠀⠀⡀⠀⢀⣿⡏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡾⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⡏⢀⣤⣴⣶⣿⣿⣿⣿⣿⣷⣄⠀⣠⣬⣿⣶⡶⠞⡻⢿⣷⣿⢿⣿⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⣠⡾⠋⠀⠀⠀⠀⢀⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣷⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠋⠀⠀⠙⠿⣿⣿⣿⣿⣿⣾⡏⠻⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⣀⣴⡾⢉⡤⠴⠒⠊⠉⣹⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣟⠉⠀⠀⠀⠀⠀⢀⣼⣿⣼⢿⠁⣼⡇⠀⠉⠻⣶⣀⠀⠀⠀⠀⠀⠀");
                System.out.println("⠀⠀⢀⣠⡾⠛⠁⠀⠀⠀⠀⠀⢀⣴⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣿⡛⢿⣿⣙⡋⠻⠟⣿⣤⣤⡤⠶⢖⣻⣿⣿⡯⠷⠟⠛⠋⠁⠀⠀⠀⠈⠙⢷⣤⣀⠀⠀⠀");
                System.out.println("⣠⣶⠿⠉⠀⠀⠀⠀⠀⠀⠀⠀⡼⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠳⢦⣍⣉⣉⣉⣍⣥⣴⠶⠾⠋⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠶⣶⣾⣿⡷⠀⠀");
                System.out.println("⠛⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠇⠀⢠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣿⡄⠀⠀");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣼⣦⣾⠟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣆⠀");
                System.out.println("⣶⣶⣤⣤⣤⣴⣶⣶⠶⠟⠛⠋⢹⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢤⣿⡆");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢻⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢻⡅");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣿⣴⣶⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣷");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣤⡼⢿⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⡶⠛⠉⠁⠀⠈⠛⠒⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡇⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣾⡶⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣟⣦⠀⣤⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣨");
                System.out.println("⠀⠀⠀⠀⠀⠀⠀⣠⣾⠟⠁⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡀⠈⠀⠻⠾⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢙");
                break;
            }
            case "list": {
                ChatUtil.notify("1) aaah");
                ChatUtil.notify("2) coming soon");
                ChatUtil.notify("3) coming soon");
                ChatUtil.notify("4) coming soon");
                ChatUtil.notify("5) coming soon");
            }
        }
    }
}
