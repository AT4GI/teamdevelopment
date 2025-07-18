import java.util.*;
import java.io.*;
import java.text.*;

// ============================
// Date Entity
// ============================
class DateInfo {
  private String date;
  public DateInfo(String date) { this.date = date; }
  public String getDate() { return date; }
}

// ============================
// RoomClass and Subclasses
// ============================
abstract class RoomClass {
  protected int price;
  protected int vacancy;
  public RoomClass(String price, String vacancy) {
    this.price = Integer.parseInt(price);
    this.vacancy = Integer.parseInt(vacancy);
  }
  public int getVacancy() { return vacancy; }
  public void decrementVacancy() { if (vacancy > 0) vacancy--; }
  public void incrementVacancy() { vacancy++; }
  public int getPrice() { return price; }
  public abstract String getType();
}
class SingleRoom extends RoomClass {
  public SingleRoom(String price, String vacancy) { super(price, vacancy); }
  public String getType() { return "Single"; }
}
class DoubleRoom extends RoomClass {
  public DoubleRoom(String price, String vacancy) { super(price, vacancy); }
  public String getType() { return "Double"; }
}
class SuiteRoom extends RoomClass {
  public SuiteRoom(String price, String vacancy) { super(price, vacancy); }
  public String getType() { return "Suite"; }
}

// ============================
// Room Entity
// ============================
class Room {
  private int roomNumber;
  private DateInfo checkinDate;
  private DateInfo checkoutDate;
  private List<RoomClass> roomClasses;
  public Room(int roomNumber, String checkinDate, String checkoutDate, List<RoomClass> roomClasses) {
    this.roomNumber = roomNumber;
    this.checkinDate = new DateInfo(checkinDate);
    this.checkoutDate = new DateInfo(checkoutDate);
    this.roomClasses = roomClasses;
  }
  public List<RoomClass> getRoomClasses() { return roomClasses; }
  public DateInfo getCheckinDate() { return checkinDate; }
  public DateInfo getCheckoutDate() { return checkoutDate; }
  public int getRoomNumber() { return roomNumber; }
}

// ============================
// Reservation Entity
// ============================
class Reservation {
  private static int counter = 1000;
  private final int reservationId;
  private int roomNumber;
  private String roomType;
  private String checkinDate;
  private String checkoutDate;
  private boolean checkedOut;
  private boolean checkedIn;

  public Reservation(int roomNumber, String roomType, String checkinDate, String checkoutDate) {
    this.reservationId = counter++;
    this.roomNumber = roomNumber;
    this.roomType = roomType;
    this.checkinDate = checkinDate;
    this.checkoutDate = checkoutDate;
    this.checkedOut = false;
    this.checkedIn = false;
  }

  public int getReservationId() { return reservationId; }
  public int getRoomNumber() { return roomNumber; }
  public String getRoomType() { return roomType; }
  public String getCheckinDate() { return checkinDate; }
  public String getCheckoutDate() { return checkoutDate; }
  public boolean isCheckedOut() { return checkedOut; }
  public boolean isCheckedIn() { return checkedIn; }
  public void setCheckedOut(boolean checkedOut) { this.checkedOut = checkedOut; }
  public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }
}

// ============================
// ReservationControl
// ============================
class ReservationControl {
  private List<Room> vacantRooms;
  private Map<Integer, Reservation> reservations;
  private final String logFilePath = "reservation.log";

  public ReservationControl() {
    this.vacantRooms = new ArrayList<>();
    this.reservations = new HashMap<>();
  }

  private void log(String message) {
    try (FileWriter fw = new FileWriter(logFilePath, true);
         BufferedWriter bw = new BufferedWriter(fw);
         PrintWriter out = new PrintWriter(bw)) {
      out.println(new Date() + " - " + message);
    } catch (IOException e) {
      System.out.println("ログファイルに書き込みできません: " + e.getMessage());
    }
  }

  public List<Room> findVacantRoom(String checkinDate, String checkoutDate, String type) {
    List<Room> matchedRooms = new ArrayList<>();
    for (Room room : vacantRooms) {
      for (RoomClass rc : room.getRoomClasses()) {
        if (rc.getType().equalsIgnoreCase(type) && rc.getVacancy() > 0) {
          matchedRooms.add(room);
          break;
        }
      }
    }
    return matchedRooms;
  }

  public int reserveRoom(int roomNumber, String type, String checkin, String checkout) {
    for (Room room : vacantRooms) {
      if (room.getRoomNumber() == roomNumber) {
        for (RoomClass rc : room.getRoomClasses()) {
          if (rc.getType().equalsIgnoreCase(type) && rc.getVacancy() > 0) {
            rc.decrementVacancy();
            Reservation r = new Reservation(roomNumber, type, checkin, checkout);
            reservations.put(r.getReservationId(), r);
            log("予約完了: 予約番号=" + r.getReservationId() + ", 部屋番号=" + roomNumber + ", 種別=" + type + ", チェックイン=" + checkin + ", チェックアウト=" + checkout);
            return r.getReservationId();
          }
        }
      }
    }
    return -1;
  }

  public void checkIn(int reservationId) {
    if (reservations.containsKey(reservationId)) {
      Reservation r = reservations.get(reservationId);
      if (r.isCheckedIn()) {
        System.out.println("すでにチェックイン済みです。");
      } else {
        r.setCheckedIn(true);
        log("チェックイン完了: 予約番号=" + reservationId + ", 部屋番号=" + r.getRoomNumber());
        System.out.println("チェックイン完了しました。部屋番号: " + r.getRoomNumber());
      }
    } else {
      System.out.println("その予約番号は存在しません。");
    }
  }

  public void checkOut(int reservationId, int roomNumber) {
    if (reservations.containsKey(reservationId)) {
      Reservation r = reservations.get(reservationId);
      if (r.getRoomNumber() != roomNumber) {
        System.out.println("部屋番号が一致しません。");
        return;
      }
      if (!r.isCheckedOut()) {
        r.setCheckedOut(true);
        log("チェックアウト完了: 予約番号=" + reservationId);
        System.out.println("チェックアウトが完了しました。");
      } else {
        System.out.println("すでにチェックアウト済みです。");
      }
    } else {
      System.out.println("予約番号が見つかりません。");
    }
  }

  public void cancelReservation(int reservationId, String type, String checkin, String checkout) {
    if (reservations.containsKey(reservationId)) {
      Reservation r = reservations.get(reservationId);
      if (r.getRoomType().equalsIgnoreCase(type) && r.getCheckinDate().equals(checkin) && r.getCheckoutDate().equals(checkout)) {
        for (Room room : vacantRooms) {
          if (room.getRoomNumber() == r.getRoomNumber()) {
            for (RoomClass rc : room.getRoomClasses()) {
              if (rc.getType().equalsIgnoreCase(type)) {
                rc.incrementVacancy();
                break;
              }
            }
          }
        }
        reservations.remove(reservationId);
        log("予約キャンセル: 予約番号=" + reservationId);
        System.out.println("予約キャンセルが完了しました。");
      } else {
        System.out.println("入力情報が一致しません。キャンセルできません。");
      }
    } else {
      System.out.println("予約番号が見つかりません。");
    }
  }

  public void addVacantRoom(Room room) {
    vacantRooms.add(room);
  }

  public boolean isValidDateFormat(String dateStr) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    sdf.setLenient(false);
    try {
      sdf.parse(dateStr);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }
}

// ============================
// ManagementControl
// ============================
class ManagementControl {
  private List<Room> rooms;
  public ManagementControl() { this.rooms = new ArrayList<>(); }
  public void registerVacantRoom(String checkinDate, String checkoutDate,
    String singlePrice, String singleVacancy,
    String doublePrice, String doubleVacancy,
    String suitePrice, String suiteVacancy) {
    SingleRoom single = new SingleRoom(singlePrice, singleVacancy);
    DoubleRoom dbl = new DoubleRoom(doublePrice, doubleVacancy);
    SuiteRoom suite = new SuiteRoom(suitePrice, suiteVacancy);
    List<RoomClass> roomClassList = new ArrayList<>();
    roomClassList.add(single); roomClassList.add(dbl); roomClassList.add(suite);
    Room room = new Room(rooms.size() + 1, checkinDate, checkoutDate, roomClassList);
    rooms.add(room);
  }
  public List<Room> getRooms() { return rooms; }
}

// ============================
// Main Entry Point
// ============================
public class HotelReservationSystem {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    ManagementControl management = new ManagementControl();
    ReservationControl reservation = new ReservationControl();

    management.registerVacantRoom("2025/07/20", "2025/07/21", "10000", "3", "15000", "2", "30000", "1");
    management.registerVacantRoom("2025/07/21", "2025/07/22", "12000", "1", "18000", "2", "32000", "1");
    for (Room room : management.getRooms()) {
      reservation.addVacantRoom(room);
    }

    while (true) {
      System.out.println("\n1. 予約\n2. チェックイン\n3. チェックアウト\n4. 予約キャンセル\n5. 終了");
      String input = scanner.nextLine();

      if (input.equals("1")) {
        String checkin, checkout;
        while (true) {
          System.out.println("チェックイン日 (YYYY/MM/DD):");
          checkin = scanner.nextLine();
          if (reservation.isValidDateFormat(checkin)) break;
          System.out.println("形式が正しくありません。再入力してください。");
        }
        while (true) {
          System.out.println("チェックアウト日 (YYYY/MM/DD):");
          checkout = scanner.nextLine();
          if (reservation.isValidDateFormat(checkout)) break;
          System.out.println("形式が正しくありません。再入力してください。");
        }
        System.out.println("部屋タイプ (Single/Double/Suite):");
        String type = scanner.nextLine();

        List<Room> candidates = reservation.findVacantRoom(checkin, checkout, type);
        if (candidates.isEmpty()) {
          System.out.println("該当する空室がありません。");
        } else {
          int selectedRoomNo = candidates.get(0).getRoomNumber();
          int resId = reservation.reserveRoom(selectedRoomNo, type, checkin, checkout);
          if (resId != -1) {
            System.out.println("予約完了、予約番号: " + resId);
          } else {
            System.out.println("予約に失敗しました。");
          }
        }

      } else if (input.equals("2")) {
        System.out.println("予約番号を入力:");
        int resId = Integer.parseInt(scanner.nextLine());
        reservation.checkIn(resId);

      } else if (input.equals("3")) {
        System.out.println("予約番号を入力:");
        int resId = Integer.parseInt(scanner.nextLine());
        System.out.println("部屋番号:");
        int roomNo = Integer.parseInt(scanner.nextLine());
        reservation.checkOut(resId, roomNo);

      } else if (input.equals("4")) {
        System.out.println("予約番号を入力:");
        int resId = Integer.parseInt(scanner.nextLine());
        System.out.println("チェックイン日 (YYYY/MM/DD):");
        String checkin = scanner.nextLine();
        System.out.println("チェックアウト日 (YYYY/MM/DD):");
        String checkout = scanner.nextLine();
        System.out.println("部屋タイプ:");
        String type = scanner.nextLine();
        reservation.cancelReservation(resId, type, checkin, checkout);

      } else if (input.equals("5")) {
        System.out.println("終了します。");
        break;
      } else {
        System.out.println("無効な選択です。");
      }
    }
  }
}
